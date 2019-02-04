from BookGetter import BookGetter, ServerError
from ReadeoDBManager import ReadeoDBManager
from string import ascii_uppercase
from string import ascii_lowercase
import configparser
import requests


"""
Updates the config starts indexes.
:param section The section to update.
:param new_first_start The first loop start to define.
:param new_second_start The second start loop to define.
"""
def update_indexes(section, new_first_start, new_second_start):
    config[section]["firstLoopStart"] = str(new_first_start)
    config[section]["secondLoopStart"] = str(new_second_start)

    with open(config_file, "w") as conf:
        config.write(conf)


"""
Called when an error occurs, displays the message, save the starts indexes and exit.
:param exception The exception raised.
:param section The section to update.
:param new_first_start The first loop start to define.
:param new_second_start The second start loop to define.
"""
def handle_error(exception, section, new_first_start, new_second_start):
    update_indexes(section, new_first_start, new_second_start)
    print(exception.message)

    exit(1)


book_getter = BookGetter("volumes", {"q": ""}, use_default=True)
db_manager = ReadeoDBManager()
config = configparser.ConfigParser()
end_loop = 25
config_file = "extract.conf"

config.read(config_file)


for elt in ["CONTENT", "AUTHOR", "TITLE", "CATEGORY"]:
    first_start = int(config[elt]["firstLoopStart"])
    second_start = int(config[elt]["secondLoopStart"])

    if first_start >= end_loop and second_start >= end_loop:
        continue

    try:
        if elt == "CONTENT":
            for upper in ascii_uppercase[first_start:]:
                second_start = 0 if (second_start >= end_loop) else second_start

                for lower in ascii_lowercase[second_start:]:
                    try:
                        book_list = book_getter.query_content_filtered_books("{}{}".format(upper, lower))

                        db_manager.insert_all(book_list)
                    except ServerError as e:
                        handle_error(e, elt, first_start, second_start)

                    second_start += 1

                first_start += 1
            # TODO: SEE IF FIXES SEC UPDATE
            update_indexes(elt, first_start, second_start)
            continue

        elif elt == "AUTHOR":
            queryFilter = BookGetter.AUTHOR_FILTER
        elif elt == "TITLE":
            queryFilter = BookGetter.TITLE_FILTER
        elif elt == "CATEGORY":
            queryFilter = BookGetter.CATEGORY_FILTER

        for upper in ascii_uppercase[first_start:]:
            second_start = 0 if (second_start >= end_loop) else second_start

            for lower in ascii_lowercase[second_start:]:
                try:
                    book_list = book_getter.query_filtered_books(queryFilter, "{}{}".format(upper, lower))

                    db_manager.insert_all(book_list)
                except ServerError as e:
                    handle_error(e, elt, first_start, second_start)

                second_start += 1

            first_start += 1

    # Catching every exception in order to save the last position.
    except Exception as e:
        handle_error(e, elt, first_start, second_start)

    except KeyboardInterrupt as k:
        handle_error(k, elt, first_start, second_start)

    update_indexes(elt, first_start, second_start)

exit(0)
