import pandas as pd
from bs4 import BeautifulSoup
import requests

results = []

# recipe url ranges (got from Supercook)
for i in range(2501, 2827):
    url = "http://allrecipes.com/recipes/?sort=Title&page=" + str(i)
    print(i)

    # get url
    r = requests.get(url)

    # parse html content
    soup = BeautifulSoup(r.content, "html.parser")

    # find article attribute and class name
    links = soup.find_all('article', {'class': "fixed-recipe-card"})
    
    # get the link in link attribute and append it to list
    for ele in links:
        results.append(ele.a["href"])

# put list into dataframe
df = pd.DataFrame({'Links': results})
df.to_csv('links.csv', index=False, encoding='utf-8')

print(df)