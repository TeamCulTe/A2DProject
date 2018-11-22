# coding: utf8

import requests
import json

"""
Class used to query Google's book API depending on the BookGetter's attributes values.
"""


class BookGetter:
    GOOGLE_BASE_API_URL = "https://www.googleapis.com/books/v1/"
    GOOGLE_API_KEY = "AIzaSyDIAmHT88KlESDtr4BZ2BWW37FJC8otmtI"

    """
    BookGetter's constructor taking a category to browse, a list of parameters and an associated list of values.
    The constructor checks if the number of parameters is equal to the number of associated values.
    :param category The category to browse
    :param parameters The dict of parameters (key) and values (value) to set.
    """
    def __init__(self, category, parameters):
        if type(parameters) != dict:
            raise TypeError("The parameters parameter should be instance of dict.")

        self.category = category
        self.parameters = parameters

    """
    Depending on the object attributes, builds and return the url to query the book API.
    """
    def generate_url(self):
        url = "{}{}?".format(BookGetter.GOOGLE_BASE_API_URL, self.category)

        for parameter, value in self.parameters.items():
            url += "{}={}&".format(parameter, value)

        url += "key={}".format(BookGetter.GOOGLE_API_KEY)

        return url

    """
    Queries the built API url to get JSON books response.
    """
    def query_books(self):
        response = requests.get(self.generate_url())

        if response.status_code != 200:
            raise Exception("An error occurred, the server returned a {} response status.")

        return json.loads(response.content, encoding="UTF-8")




b = BookGetter("volumes", {"q": "A"})
b.query_books()