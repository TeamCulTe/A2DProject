from BookGetter import BookGetter, ServerError
from ReadeoDBManager import ReadeoDBManager
from string import ascii_uppercase
from string import ascii_lowercase
import configparser

book_getter = BookGetter("volumes", {"q": ""}, use_default=True)
db_manager = ReadeoDBManager()
config = configparser.ConfigParser()
end_loop = 25
config_file = "extract.conf"

config.read(config_file)


def update_indexes(section, new_first_start, new_second_start):
    config[section]["firstLoopStart"] = str(new_first_start)
    config[section]["secondLoopStart"] = str(new_second_start)

    with open(config_file, "w") as conf:
        config.write(conf)


for elt in ["CONTENT", "AUTHOR", "TITLE", "CATEGORY"]:
    first_start = int(config[elt]["firstLoopStart"])
    second_start = int(config[elt]["secondLoopStart"])

    if first_start > end_loop and second_start > end_loop:
        continue

    if elt == "CONTENT":
        for upper in ascii_uppercase[first_start:]:
            for lower in ascii_lowercase[second_start:]:
                try:
                    book_list = book_getter.query_content_filtered_books("{}{}".format(upper, lower))

                    db_manager.insert_all(book_list)
                except ServerError as e:
                    update_indexes(elt, first_start, second_start)
                    print(e.message)
                    exit(1)

                second_start += 1

            second_start = 0
            first_start += 1

        continue

    elif elt == "AUTHOR":
        queryFilter = BookGetter.AUTHOR_FILTER
    elif elt == "TITLE":
        queryFilter = BookGetter.TITLE_FILTER
    elif elt == "CATEGORY":
        queryFilter = BookGetter.CATEGORY_FILTER

    for upper in ascii_uppercase[first_start:]:
        for lower in ascii_lowercase[second_start:]:
            try:
                book_list = book_getter.query_filtered_books(queryFilter, "{}{}".format(upper, lower))

                db_manager.insert_all(book_list)
            except ServerError as e:
                print(e.message)
                update_indexes(elt, first_start, second_start)
                exit(1)

            second_start += 1

        second_start = 0
        first_start += 1

exit(0)
