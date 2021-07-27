from recipe_scrapers import scrape_me
import pandas as pd
import csv

# list of ingredients from Supercook
list = ['pepper', 'beans', ':', 'batter', 'cranberries', 'olive oil', 'vegetable oil','shallot', 'spray', 'water', 'toppings', 'sauce', 'spice', 'mint', 'dough', 'butter', 'seed', 'basil', 'oregano', 'thyme', 'chocolate', 'vanilla', 'baking powder', 'butter milk', 'cocoa', 'cheese', 'custard', 'egg', 'ice cream', 'milk', 'sour cream', 'yogurt', 'apple', 'banana', 'blackberries', 'blueberries', 'cherries', 'coconut', 'cranberries', 'salt', 'grapes', 'kiwi', 'lemon', 'lime', 'mango', 'orange', 'peach', 'pear', 'pineapple', 'raspberries', 'strawberries', 'baking soda', 'bread', 'flour', 'noodles', 'pasta', 'rice', 'yeast', 'beef soup', 'chicken soup', 'beer', 'lamb soup', 'oil', 'red wine', 'white wine', 'bacon', 'beef', 'chicken', 'ham', 'lamb', 'pork', 'salami', 'sausage', 'turkey', 'almond', 'cashew', 'macadamia', 'peanut', 'peanut butter', 'walnut', 'carp', 'catfish', 'crab', 'eel', 'lobster', 'mackerel', 'mussel', 'oyster', 'prawn', 'salmon', 'sardine', 'scallop', 'shrimp', 'squid', 'trout', 'tuna', 'barbeque sauce', 'syrup', 'fish sauce', 'honey', 'mayonnaise', 'mustard', 'soy sauce', 'vinegar', 'sugar', 'avocado', 'broccoli', 'cabbage', 'carrot', 'cauliflower', 'celery', 'corn', 'cucumber', 'eggplant', 'garlic', 'ginger', 'lettuce', 'mushroom', 'olive', 'onion', 'pickle', 'potato', 'pumpkin', 'spinach', 'sweet potato', 'tomato', 'zucchini', 'asparagus']

import sys
sys.setrecursionlimit(6600)

df = pd.read_csv("links.csv")

data = df.Links.to_list()

# create dataframe with link, title of recipe, list of ingredients
# df = pd.DataFrame(columns=['Link', 'Title', 'Ingredients'])


contain = []

with open('recipes.csv','a', newline='') as fd:
    for recipe in data[:325]:
        print("https:" + recipe)
        scraper = scrape_me("https:" + recipe)
        ingredients = scraper.ingredients()
        title = scraper.title()
        #print(title)
        

        for ingredient in ingredients:
            
            ingredientLower = ingredient.lower()

            # if substring in ingredients are in list
            if any(substring.lower() in ingredientLower for substring in list):
                for substring in list:
                    while substring.lower() in ingredientLower:
                        # append to list
                        contain.append(substring)
                        break

        if (len(contain) != 0):
            # append to dataframe
            writer = csv.writer(fd)
            appendData = [str(recipe), str(title), str(contain)]
            writer.writerow(appendData)
            contain = []

fd.close()
    # # write to csv
    # df.to_csv('recipes.csv', index=False, encoding='utf-8')
