# coding: utf8

import requests
import json

"""
Class used to query Google's book API depending on the BookGetter's attributes values.
"""


class BookGetter:
    GOOGLE_BASE_API_URL = "https://www.googleapis.com/books/v1/"
    GOOGLE_API_KEY = "AIzaSyDIAmHT88KlESDtr4BZ2BWW37FJC8otmtI"
    DEFAULT_PARAMETERS = {"langRestrict": "en", "printType": "books"}

    """
    BookGetter's constructor taking a category to browse, a list of parameters and an associated list of values.
    The constructor checks if the number of parameters is equal to the number of associated values.
    :param category The category to browse
    :param parameters The dict of parameters (key) and values (value) to set.
    """
    def __init__(self, category, parameters={}, use_default=False):
        if type(parameters) != dict:
            raise TypeError("The parameters parameter should be instance of dict.")

        self.category = category

        if use_default:
            parameters.update(BookGetter.DEFAULT_PARAMETERS)

        self.parameters = parameters

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
    Update the parameter attribute in order to add a filter on author's name.
    :param value The string to filter from author name.
    """
    def add_author_filter(self, value):
        author_filter = "+inauthor:{}".format(value)

        if "q" in self.parameters.keys():
            self.parameters["q"] = author_filter
        else:
            self.parameters.update({"q": author_filter})

    """
    Queries the built API url to get JSON books response.
    :return The json response of the querry.
    """
    def query_books(self, log=False):
        url = self.generate_url()
        response = requests.get(url)

        if response.status_code != 200:
            raise Exception("An error occurred contacting {}.\nThe server returned a {} response status.".format(url,
                            response.status_code))

        if log:
            print("Queried URL : {}\n".format(url))

        return json.loads(response.content, encoding="UTF-8")

    """
    From a list of books in json format parameter, extract the needed information.
    :return Only the needed information from each books (title, authors, categories, summary, cover, publication date).
    """
    @staticmethod
    def extract_books_info(json):
        book_lst = []
        for elt in json["items"]:
            data = elt["volumeInfo"]
            keys = data.keys()
            summary = data["description"] if "description" in keys else None
            categories = data["categories"] if "categories" in keys else None
            authors = data["authors"] if "authors" in keys else None
            cover = data["imageLinks"]["smallThumbnail"] if "imageLinks" in keys else None
            date_published = data["publishedDate"] if len(data["publishedDate"]) >= 4 else data["publishedDate"][:4]
            current_book = {"title": data["title"], "author": authors, "date_published": date_published,
                            "cover": cover, "summary": summary, "category": categories}

            book_lst.append(current_book)

        return book_lst

    """
    From a string filter on an author name, query the book API and return the needed information.
    """
    def full_query_from_author(self, value):
        self.add_author_filter(value)
        return self.extract_books_info(self.query_books())


b = BookGetter("volumes", {"q": "+inauthor:A"})
for book in b.extract_books_info(b.query_books(log=True)):
    print book
