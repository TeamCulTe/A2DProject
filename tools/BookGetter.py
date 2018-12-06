# coding: utf8

import requests
import json
import ReadeoDBManager

"""
Class used to query Google's book API depending on the BookGetter's attributes values.
"""


class BookGetter:
    # Base API configuration values.
    GOOGLE_BASE_API_URL = "https://www.googleapis.com/books/v1/"
    GOOGLE_API_KEY = "AIzaSyDIAmHT88KlESDtr4BZ2BWW37FJC8otmtI"

    # Common browsing parameters keys.
    RESULTS_PARAMETER = "maxResults"
    INDEX_PARAMETER = "startIndex"

    # Default pagination values.
    MAX_PAGINATION = 40
    DEFAULT_PAGINATION = 10

    # Default query parameters.
    DEFAULT_PARAMETERS = {"printType": "books", INDEX_PARAMETER: 0,
                          RESULTS_PARAMETER: MAX_PAGINATION}

    # Defaults filters values (need to be formatted).
    AUTHOR_FILTER = "+inauthor:{}"
    TITLE_FILTER = "+intitle:{}"
    CATEGORY_FILTER = "+subject:{}"

    """
    BookGetter's constructor taking a category to browse, a list of parameters and an associated list of values.
    The constructor checks if the parameter "parameters" is a dict and initializes the number of results (pagination 
    use).
    :param category The category to browse
    :param parameters The dict of parameters (key) and values (value) to set.
    :param use_default ? Defines if using the default parameters or not
    """
    def __init__(self, category, parameters, use_default=True):
        if type(parameters) != dict:
            raise TypeError("The parameters parameter should be instance of dict.")

        self.category = category
        self.results = 0

        if use_default:
            parameters.update(BookGetter.DEFAULT_PARAMETERS)
            self.pagination = BookGetter.MAX_PAGINATION
        else:
            if BookGetter.RESULTS_PARAMETER in parameters.keys():
                self.pagination = parameters[BookGetter.RESULTS_PARAMETER]
            else:
                self.pagination = BookGetter.DEFAULT_PAGINATION

        self.parameters = parameters

    """
    Adds a parameter and its value to the parameters attribute and update the pagination if needed.
    :param key The name of the parameter.
    :param value The associated value.
    """
    def add_parameter(self, key, value):
        self.parameters[key] = value
        if key == BookGetter.RESULTS_PARAMETER:
            self.pagination = value

    """
    Adds a filter to the parameters (q) if it exists else update it.
    :param filter_type The type of filter to apply (author, title, category).
    :param filter_value The string filter to apply.
    """
    def add_filter(self, filter_type, filter_value):
        full_filter = filter_type.format(filter_value)
        self.add_parameter("q", full_filter)

    """
    Depending on the object attributes, builds and return the url to query the book API.
    :return The url to query the book API.
    """
    def generate_url(self):
        url = "{}{}?".format(BookGetter.GOOGLE_BASE_API_URL, self.category)

        for parameter, value in self.parameters.items():
            url += "{}={}&".format(parameter, value)

        url += "key={}".format(BookGetter.GOOGLE_API_KEY)

        return url

    """
    Update the BookGetter's pagination depending on the current index and the maximum pagination results.
    :return True if last result reached else False
    """
    def update_pagination(self):
        end = False
        offset = self.parameters[BookGetter.INDEX_PARAMETER] + self.pagination

        if self.parameters[BookGetter.INDEX_PARAMETER] >= self.results:
            offset = 0
            end = True

        self.parameters[BookGetter.INDEX_PARAMETER] = offset

        return end

    """
    From a list of books in json format parameter, extract the needed information.
    :param json_books The list of books from which extract the information.
    :param book_list ? The list in which append the book's info.
    :return Only the needed information from each books (title, authors, categories, summary, cover, publication date).
    """
    @staticmethod
    def extract_books_info(json_books, book_lst=None):
        if book_lst is None:
            book_lst = []

        if "items" not in json_books:
            return book_lst

        for elt in json_books["items"]:
            data = elt["volumeInfo"]
            keys = data.keys()
            summary = data["description"] if "description" in keys else None
            category = data["categories"][0] if "categories" in keys else None
            authors = data["authors"] if "authors" in keys else None
            cover = data["imageLinks"]["smallThumbnail"] if "imageLinks" in keys else None
            date_published = (data["publishedDate"] if len(data["publishedDate"]) <= 4 else data["publishedDate"][:4]) \
                if "publishedDate" in keys else None

            if category is None or authors is None or date_published is None or summary is None or cover is None:
                continue

            current_book = {"title": data["title"], "authors": authors, "date_published": date_published,
                            "cover": cover, "summary": summary, "category": category}

            book_lst.append(current_book)

        return book_lst

    """
    Queries the built API url to get JSON books response filtered to get only the needed info. Also define the 
    results attribute in order to paginate and query all the books.
    :param log ? Defines if the URL used to query the API is displayed or not.
    :return The json response of the query.
    """
    def query_books(self, log=False):
        end = False
        first_query = True
        books = []

        while not end:
            url = self.generate_url()
            response = requests.get(url)

            if response.status_code != 200:
                raise Exception("An error occurred contacting {}.\nThe server returned a {} response status.".format(
                    url, response.status_code))

            if log:
                print("Queried URL : {}\n".format(url))

            results = json.loads(response.content, encoding="UTF-8")

            if first_query:
                self.results = results["totalItems"]
                first_query = False

            if self.results == 0:
                print("No results found.")
                break

            end = self.update_pagination()

            self.extract_books_info(results, books)

        return books

    """
    From a filter type, query the book API and return the needed information.
    :param filter_type The type of filter to apply (author, title, category).
    :param filter_value The string filter to apply.
    """
    def query_filtered_books(self, filter_key, filter_value):
        self.add_filter(filter_key, filter_value)
        return self.query_books(True)


b = BookGetter("volumes", {"q": "+intitle:\"Was\""}, use_default=True)
r = ReadeoDBManager.ReadeoDBManager()
r.insert_all(b.query_books())
r.close_connector()
for book in b.query_books():
    print book

