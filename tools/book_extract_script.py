from BookGetter import BookGetter
from ReadeoDBManager import ReadeoDBManager
from string import ascii_uppercase
from string import ascii_lowercase

book_getter = BookGetter("volumes", {"q": ""}, use_default=True)
db_manager = ReadeoDBManager()

# Query all books on content from A to Z
for upper in ascii_uppercase:
    for lower in ascii_lowercase:
        book_list = book_getter.query_content_filtered_books("{}{}".format(upper, lower))
        db_manager.insert_all(book_list)

# Query all books on author name from A to Z
for upper in ascii_uppercase:
    for lower in ascii_lowercase:
        book_list = book_getter.query_filtered_books(BookGetter.AUTHOR_FILTER, "{}{}".format(upper, lower))
        db_manager.insert_all(book_list)

# Query all books on title name from A to Z
for upper in ascii_uppercase:
    for lower in ascii_lowercase:
        book_list = book_getter.query_filtered_books(BookGetter.TITLE_FILTER, "{}{}".format(upper, lower))
        db_manager.insert_all(book_list)

# Query all books on category name from A to Z
for upper in ascii_uppercase:
    for lower in ascii_lowercase:
        book_list = book_getter.query_filtered_books(BookGetter.CATEGORY_FILTER, "{}{}".format(upper, lower))
        db_manager.insert_all(book_list)