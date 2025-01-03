# Use the official Ollama image as the base
FROM ollama/ollama:latest

# Set the working directory
WORKDIR /root/.ollama

# Install a new model (replace 'llama2' with the model you want to install)
RUN ollama serve & ollama list && ollama pull qwen2.5-coder:0.5b

# Expose the necessary port
EXPOSE 11434

# Command to run the Ollama service

