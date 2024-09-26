# Models of Complex Drug Treatments for Viral Infections 💊🦠

This repository contains computational models exploring the effects of complex drug treatments on viral infections. The primary focus is on understanding how antiviral drugs, such as **Nirmatrelvir**, interact with viruses like SARS-CoV-2. These models help simulate and predict the outcomes of various drug administration strategies.

## Contributors 👥

This project was developed in collaboration with:

- [David Galfi](https://github.com/davidgalfi)
- [barfer](https://github.com/barfer)
- [msjuhasznora](https://github.com/msjuhasznora)

## Features

- Simulates drug treatment efficacy over time
- Models viral load reduction using differential equations
- Allows customization of drug dosage and treatment schedules

## Table of Contents

- [Installation](#installation)
- [Usage](#usage)
- [Project Structure](#project-structure)
- [Contributing](#contributing)
- [License](#license)

## Installation 🛠️

1. Clone the repository:

    ```bash
    git clone https://github.com/davidgalfi/Models-of-Complex-Drug-Treatments-for-Viral-Infections.git
    ```

2. Navigate to the project directory:

    ```bash
    cd Models-of-Complex-Drug-Treatments-for-Viral-Infections
    ```

3. Download the **HAL Library** from [HAL setup page](https://halloworld.org/setup.html).

4. Include the HAL library in your project by adding it to your classpath:

    ```bash
    javac -cp "path-to-hal-library.jar" src/Main.java
    ```

5. Ensure you have Java installed. If not, install the latest version [here](https://www.java.com/en/download/).

## Usage 🚀

1. Compile the Java source code with the HAL library:

    ```bash
    javac -cp "path-to-hal-library.jar" src/Main.java
    ```

2. Run the main simulation:

    ```bash
    java -cp "path-to-hal-library.jar:." Main
    ```

3. Adjust parameters in the `config.json` file to simulate different drug treatment regimens.

## Project Structure 📁

- **src/**: Contains the Java source code for the model simulations.
- **data/**: Includes experimental results and example datasets.
- **config/**: Configuration files for customizable parameters.
- **output/**: Stores the output from model simulations.

## Contributing 🤝

Contributions are welcome! Feel free to submit issues or pull requests. Please make sure to follow the coding standards and include tests when adding new features.

## License 📄

This project is licensed under the MIT License. See the [LICENSE](./LICENSE) file for more details.

---

Happy modeling! 😊
