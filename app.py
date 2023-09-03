import tensorflow as tf
from flask import Flask, request, jsonify
from flask_cors import CORS

app = Flask(__name__)
CORS(app)

model = tf.keras.applications.MobileNetV2(weights='imagenet', include_top=True)

@app.route('/predict', methods=['POST'])
def predict():
    try:
        image = tf.io.decode_image(request.files['image'].read(), channels=3)
        image = tf.image.resize(image, [224, 224])
        image = tf.expand_dims(image, axis=0)
        image = image / 255.0

        predictions = model.predict(image)
        if isinstance(predictions, tf.Tensor):
            predictions = predictions.numpy()

        decoded_predictions = tf.keras.applications.mobilenet_v2.decode_predictions(predictions)
        top_predictions = decoded_predictions[0]

        for pred in top_predictions:
            label = pred[1].lower()
            confidence = float(pred[2]) * 100

            if any(substring in label for substring in ['dog', 'hound', 'terrier', 'retriever', 'bulldog', 'shepherd', 'spaniel', 'poodle', 'collie', 'malamute', 'husky', 'pinscher', 'mastiff', 'boxer', 'chihuahua', 'dalmatian', 'pointer', 'greyhound', 'pyrenees', 'schipperke', 'komondor', 'kuvasz', 'kelpie', 'malinois', 'briard', 'papillon', 'groenendael', 'eskimo']):
                return jsonify(result='Detectado: Cachorro', confidence=f'{confidence:.2f}%')
            elif any(substring in label for substring in ['cat', 'persian', 'siamese', 'egyptian', 'cougar', 'lynx', 'leopard', 'lion', 'tiger', 'cheetah']):
                return jsonify(result='Detectado: Gato', confidence=f'{confidence:.2f}%')

        return jsonify(result='Nenhum cão ou gato detectado.')
    except Exception as e:
        app.logger.error(f"Erro ao processar a solicitação: {e}")
        return jsonify(error=str(e)), 400

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)