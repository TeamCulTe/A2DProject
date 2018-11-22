import mysql.connector


"""
Database manager class used to connect and store data into the database.
"""


class DbManager:
    DB_NAME = "Readeo"
    DB_USER = "root"
    DB_PASSWORD = "root"
    DB_HOST = "localhost:8889"

    TBL_AUTHOR = {"name": "Author", "fields": ["id_author", "name_author", "deleted"]}
    TBL_BOOK = {"name": "Book", "fields": ["id_book", "id_author", "id_book", "title", "cover", "summary",
                                           "date_published", "deleted"]}
    TBL_CATEGORY = {"name": "Category", "fields": ["id_category", "name_category", "deleted"]}
    TBL_COUNTRY = {"name": "Country", "fields": ["id_country", "name_country", "deleted"]}
    TBL_WRITER = {"name": "Writer", "fields": ["id_author", "id_book"]}

    """
    DbManager's constructor getting the required info to connect to a database.
    :param host The host to connect.
    :param user The user who's getting connected.
    :param password The password of the user trying to connect.
    :param db The database to connect.
    """
    def __init__(self, host=DB_HOST, user=DB_USER, password=DB_PASSWORD, db=DB_NAME):
        self.host = host
        self.user = user
        self.password = password
        self.database = db
        self.connector = None

    """
    Initializes the connector attribute.
    """
    def get_connector(self):
        self.connector = mysql.connector.connect(host=self.host, user=self.user, password=self.password,
                                                 database=self.database)

    """
    Closes the connector attribute.
    """
    def close_connector(self):
        self.connector.close()
