from BookGetter import BookGetter
from ReadeoDBManager import ReadeoDBManager
from string import ascii_uppercase

book_getter = BookGetter("volumes", {"q": ""}, use_default=True)
db_manager = ReadeoDBManager()

# Query all books on author name from A to Z
for letter in ascii_uppercase:
    book_list = book_getter.query_filtered_books(BookGetter.AUTHOR_FILTER, letter)
    db_manager.insert_all(book_list)

# Query all books on title name from A to Z
for letter in ascii_uppercase:
    book_list = book_getter.query_filtered_books(BookGetter.TITLE_FILTER, letter)
    db_manager.insert_all(book_list)

# Query all books on category name from A to Z
for letter in ascii_uppercase:
    book_list = book_getter.query_filtered_books(BookGetter.CATEGORY_FILTER, letter)
    db_manager.insert_all(book_list)