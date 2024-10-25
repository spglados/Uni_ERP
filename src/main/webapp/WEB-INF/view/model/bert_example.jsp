<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>BERT in JSP Example</title>
</head>
<body>
<h1>BERT Model in JSP with JavaScript</h1>
<p>Enter a sentence with a missing word (use [MASK] to denote the missing word):</p>
<input type="text" id="input-text" value="The capital of France is [MASK]." style="width: 80%;">
<button onclick="runBERT()">Predict</button>

<h3>Prediction:</h3>
<pre id="output"></pre>

<!-- Include the @xenova/transformers library from a CDN -->
<script src="https://cdn.jsdelivr.net/npm/@xenova/transformers@1.0.0/dist/transformers.min.js"></script>

<script>
    // Define an async function to run BERT
    async function runBERT() {
        // Get the input text from the HTML input field
        const inputText = document.getElementById('input-text').value;

        // Load the pre-trained BERT model and tokenizer
        const model = await transformers.load('bert-base-uncased');
        const tokenizer = await transformers.loadTokenizer('bert-base-uncased');

        // Encode the input text
        const inputs = tokenizer.encode(inputText, {
            return_tensors: 'pt',
            add_special_tokens: true
        });

        // Perform inference using the model
        const outputs = await model(inputs);

        // Get the logits for the masked token ([MASK]) position
        const maskedIndex = inputs.indexOf(tokenizer.mask_token_id);
        const logits = outputs.logits[0][maskedIndex];

        // Get the top 5 predicted tokens for the [MASK] position
        const topTokens = logits.topk(5).indices;

        // Convert token IDs to human-readable words
        const predictions = topTokens.map(tokenId => tokenizer.decode([tokenId]));

        // Display the predictions in the HTML output
        document.getElementById('output').textContent = predictions.join(', ');
    }
</script>
</body>
</html>
