import mysql.connector


"""
Database manager class used to connect and store data into the database.
"""


class ReadeoDBManager:
    DB_NAME = "Readeo"
    DB_USER = "root"
    DB_PASSWORD = "root"
    DB_SOCKET = "/Applications/MAMP/tmp/mysql/mysql.sock"

    TBL_AUTHOR = {"name": "Author", "fields": ["id_author", "name_author", "deleted"]}
    TBL_BOOK = {"name": "Book", "fields": ["id_book", "id_category", "title", "cover", "summary", "date_published",
                                           "deleted"]}
    TBL_CATEGORY = {"name": "Category", "fields": ["id_category", "name_category", "deleted"]}
    TBL_WRITER = {"name": "Writer", "fields": ["id_author", "id_book", "deleted"]}

    """
    ReadeoDBManager's constructor getting the required info to connect to a database.
    :param host The host to connect.
    :param user The user who's getting connected.
    :param password The password of the user trying to connect.
    :param db The database to connect.
    """
    def __init__(self, socket=DB_SOCKET, user=DB_USER, password=DB_PASSWORD, db=DB_NAME):
        self.socket = socket
        self.user = user
        self.password = password
        self.database = db
        self.connector = None

        self.get_connector()

    """
    Initializes the connector attribute.
    """
    def get_connector(self):
        self.connector = mysql.connector.connect(unix_socket=self.socket, user=self.user, password=self.password,
                                                 database=self.database)

    """
    Closes the connector attribute.
    """
    def close_connector(self):
        self.connector.close()

    """
    From a dict of information on a book obtained by BookGetter instance, stores the author(s) into database.
    :param book_dict The dictionary containing information on a book.
    """
    def insert_authors(self, book_dict):
        cursor = self.connector.cursor()
        query = """INSERT INTO {} ({}) VALUE(%s)""".format(ReadeoDBManager.TBL_AUTHOR["name"],
                                                           ReadeoDBManager.TBL_AUTHOR["fields"][1])
        for author in book_dict["authors"]:
            try:
                cursor.execute(query, (author,))
            except mysql.connector.errors.IntegrityError as e:
                print(e)
                continue

        self.connector.commit()
        cursor.close()

    """
    From a dict of information on a book obtained by BookGetter instance, stores the category (first one retained) 
    into database.
    :param book_dict The dictionary containing information on a book.
    """
    def insert_category(self, book_dict):
        cursor = self.connector.cursor()
        query = """INSERT INTO {} ({}) VALUE(%s)""".format(ReadeoDBManager.TBL_CATEGORY["name"],
                                                           ReadeoDBManager.TBL_CATEGORY["fields"][1])

        try:
            cursor.execute(query, (book_dict["category"],))
        except mysql.connector.errors.IntegrityError as e:
            print(e)
            return

        self.connector.commit()
        cursor.close()

    """
    From a dict of information on a book obtained by BookGetter instance, stores the book into database.
    :param book_dict The dictionary containing information on a book.
    """
    def insert_book(self, book_dict):
        cursor = self.connector.cursor()
        category_query = """SELECT {} FROM {} WHERE {} = %s""".format(ReadeoDBManager.TBL_CATEGORY["fields"][0],
                                                                      ReadeoDBManager.TBL_CATEGORY["name"],
                                                                      ReadeoDBManager.TBL_CATEGORY["fields"][1])
        cursor.execute(category_query, (book_dict["category"],))

        category_id = cursor.fetchone()

        if category_id is None:
            print(u"Error while querying \"{}\" category id.\n".format(book_dict["category"]))
            return

        book_fields = ReadeoDBManager.TBL_BOOK["fields"]
        book_query = """INSERT INTO {} ({}, {}, {}, {}, {}) VALUES({}, %s, %s, %s, %s)""".format(
            ReadeoDBManager.TBL_BOOK["name"], book_fields[1], book_fields[2], book_fields[3],
            book_fields[4], book_fields[5], category_id[0])

        try:
            cursor.execute(book_query, (book_dict["title"], book_dict["cover"], book_dict["summary"],
                           book_dict["date_published"]))
        except mysql.connector.errors.IntegrityError as e:
            print(e)
            return

        self.connector.commit()
        cursor.close()

    """
    From a dict of information on a book obtained by BookGetter instance, stores the book's writers into database.
    :param book_dict The dictionary containing information on a book.
    """
    def insert_writers(self, book_dict):
        cursor = self.connector.cursor()

        book_fields = ReadeoDBManager.TBL_BOOK["fields"]
        book_query = """SELECT {} FROM {} WHERE {} = %s AND {} = %s""".format(book_fields[0],
                                                                              ReadeoDBManager.TBL_BOOK["name"],
                                                                              book_fields[2], book_fields[5])
        cursor.execute(book_query, (book_dict["title"], book_dict["date_published"]))

        book_id = cursor.fetchone()

        if book_id is None:
            print(u"Error while querying \"{}\" book written in {} id.\n".format(book_dict["title"],
                                                                                book_dict["date_published"]))
            return

        author_fields = ReadeoDBManager.TBL_AUTHOR["fields"]
        author_query = """SELECT {} FROM {} WHERE {} = %s""".format(author_fields[0],
                                                                    ReadeoDBManager.TBL_AUTHOR["name"],
                                                                    author_fields[1])
        authors_id = []

        for author in book_dict["authors"]:
            cursor.execute(author_query, (author,))

            author_id = cursor.fetchone()

            if author_id is None:
                print(u"Error while querying \"{}\" author id.\n".format(author))
                continue
            authors_id.append(author_id[0])

        writer_fields = ReadeoDBManager.TBL_WRITER["fields"]
        writer_query = """INSERT INTO {} ({}, {}) VALUES(%s, %s)""".format(ReadeoDBManager.TBL_WRITER["name"],
                                                                           writer_fields[0], writer_fields[1])

        for author_id in authors_id:
            try:
                cursor.execute(writer_query, (author_id, book_id[0]))
            except mysql.connector.errors.IntegrityError as e:
                print(e)
                continue

        self.connector.commit()
        cursor.close()

    """
    From a list of dict of information on books obtained by BookGetter object, stores all the data into the associated 
    tables.
    :param book_dicts The list of dictionary containing the information on books.
    """
    def insert_all(self, book_dicts):
        if type(book_dicts) is not list:
            raise TypeError("The parameter should be a list.")

        for book_dict in book_dicts:
            if type(book_dict) is not dict:
                raise TypeError("The list should contains only dict objects.")

            try:
                self.insert_authors(book_dict)
                self.insert_category(book_dict)
                self.insert_book(book_dict)
                self.insert_writers(book_dict)
            except UnicodeDecodeError or UnicodeEncodeError as e:
                print("An error append while decoding unicode, aborting current book.\n")
                print(e)
