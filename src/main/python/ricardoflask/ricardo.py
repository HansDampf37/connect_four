from flask import Flask, request, jsonify
import torch
from PIL import Image
from torchvision import transforms
import numpy as np
import matplotlib.pyplot as plt

app = Flask(__name__)
model = torch.jit.load("/home/adrian/Dev/connect_four/src/main/python/ricardo.pt")
model.eval()  # Set the model to evaluation mode

transform = transforms.Compose([
    transforms.Resize((6, 7)),
    transforms.ToTensor()
])


@app.route('/hello-world', methods=['GET'])
def test():
    return "hello-world"


@app.route('/echo', methods=['GET'])
def echo():
    return request.json


@app.route('/predict_column', methods=['POST'])
def predict_column():
    data = request.json
    board = np.array(data['board']).astype(float)
    current_player = data['current_player']

    # Preprocess input
    input_tensor = torch.tensor(board).unsqueeze(0).unsqueeze(0).float()  # Add batch and channel dimensions
    player_tensor = torch.tensor([current_player]).float()

    # Forward pass through the model
    with torch.no_grad():
        output = model(input_tensor, player_tensor)

    predicted_column = torch.argmax(output).item()

    plt.title(f"${['black', 'white'][int(current_player)]}:${predicted_column}")
    plt.imshow(board, cmap='gray', vmin=0, vmax=1)
    plt.show()
    return jsonify({'predicted_column': predicted_column})


if __name__ == '__main__':
    app.run(debug=True)
