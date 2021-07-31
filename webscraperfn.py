import pandas as pd
from bs4 import BeautifulSoup
import requests

results = []

# recipe url ranges (got from Supercook)
for i in range(17, 47):
    url = "https://www.foodnetwork.com/recipes/recipes-a-z/b/p/" + str(i)
    print(url)

    # get url
    r = requests.get(url)

    # parse html content
    soup = BeautifulSoup(r.content, "html.parser")

    # find article attribute and class name
    links = soup.find_all('li', {'class': "m-PromoList__a-ListItem"})
    
    # get the link in link attribute and append it to list
    for ele in links:
        results.append(ele.a["href"])

# put list into dataframe
df = pd.DataFrame({'Links': results})
df.to_csv('links.csv', index=False, encoding='utf-8')

print(df)