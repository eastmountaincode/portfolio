PROBLEM:

I wanted to do a couple things with this project:

- Learn how to train a text generation model
- Learn how to actually deploy a model on a website


OUTCOME:

A friend told me about Streamlit, a platform helpful for deploying data-oriented apps, which was perfect for my foray into
text generation on the web. I got to learn about caching; everytime the button was clicked to generate new text, the 510MB model was being loaded from
my account on HuggingFace. This caused my app to crash every 2-3 generation requests. With caching, the model is loaded onto the server only once,
keeping at the app online even after many generation requests. The code file where this occurs is the duneGeneration.py file.

The site is live at: https://eastmountaincode-dunegeneration2-dunegeneration-6szy60.streamlitapp.com/

![Screen Shot 2022-07-17 at 12 35 46 PM](https://user-images.githubusercontent.com/59405316/179412651-d4f765b8-e7f2-47a3-bf5e-a806613eba5a.png)
