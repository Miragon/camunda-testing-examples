<a id="readme-top"></a>

<!-- PROJECT SHIELDS -->
<!--
*** I'm using markdown "reference style" links for readability.
*** Reference links are enclosed in brackets [ ] instead of parentheses ( ).
*** See the bottom of this document for the declaration of the reference variables
*** for contributors-url, forks-url, etc. This is an optional, concise syntax you may use.
*** https://www.markdownguide.org/basic-syntax/#reference-style-links
-->
[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![Unlicense License][license-shield]][license-url]
[![LinkedIn][linkedin-shield]][linkedin-url]


<!-- PROJECT LOGO -->
<br />
<div style="text-align: center">
  <a href="https://www.miragon.io">
    <img src="logo.svg" alt="Logo" width="80" height="80">
  </a>

<h3 align="center">Camunda 7 Testing Example</h3>

  <p style="text-align: center">
    An awesome template to jumpstart your testing projects!
    <br />
    <a href="https://github.com/othneildrew/Best-README-Template"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="https://github.com/othneildrew/Best-README-Template">View Demo</a>
    &middot;
    <a href="https://github.com/Miragon/camunda-testing-examples/issues/new?labels=bug&template=bug-report---.md">Report Bug</a>
    &middot;
    <a href="https://github.com/Miragon/camunda-testing-examples/issues/new?labels=enhancement&template=feature-request---.md">Request Feature</a>
  </p>
</div>


<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#acknowledgments">Acknowledgments</a></li>
  </ol>
</details>


<!-- ABOUT THE PROJECT -->

## About The Project

There are not many camunda 7 workflow testing examples available on GitHub. However, here there is one.

Of course, no one example will serve all BPMN process models. So feel free to
suggest changes by forking this repo and creating a pull request or opening an issue. Thanks to all
the people have contributed to expanding this repository!

Use the `IDEAS` to get started.

<p style="text-align: right">(<a href="#readme-top">back to top</a>)</p>

### Built With

the help of the following major frameworks/libraries among others.

* [![JUnit-shield]][JUnit-url]
* [![Camunda-shield-test-coverage]][Camunda7-test-coverage-url]
* [![Camunda-shield-extension]][Camunda-extension-url]

<p style="text-align: right">(<a href="#readme-top">back to top</a>)</p>


<!-- GETTING STARTED -->

## Getting Started

This is a guideline on setting up your project locally.

### Prerequisites

* Intellij or other IDE

### Installation

This project doesn't rely on any external dependencies or services.

1. Clone the repo
   ```sh
   git clone https://github.com/github_username/repo_name.git
   ```
2. Change git remote url to avoid accidental pushes to base project
   ```sh
   git remote set-url origin github_username/repo_name
   git remote -v # confirm the changes
   ```

<p style="text-align: right">(<a href="#readme-top">back to top</a>)</p>



<!-- USAGE EXAMPLES -->

## Usage

This project is meant to provide you examples how to unit test a BPMN process model.
It will focus on giving you an idea of how to unit test BPMN process models.
(It contains a running camunda starter application which we do not focus on.)

### Order structure

src/main/java/io.miragon.camunda.order/adpater/

src/main/java/io.miragon.camunda.order/

src/main/resources/

src/test/java/io.miragon.camunda/adapter/camunda/

src/test/java/io.miragon.camunda/adapter/utilities/

src/test/resources/

target/process-test-coverage/


<p style="text-align: right">(<a href="#readme-top">back to top</a>)</p>


<!-- ROADMAP -->

## Roadmap

- [ ] Add Additional Templates/Examples
- [ ] ...

See the [open issues](https://github.com/Miragon/camunda-testing-examples/issues) for a full list of proposed features (
and known issues).

<p style="text-align: right">(<a href="#readme-top">back to top</a>)</p>



<!-- CONTRIBUTING -->

## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any
contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also
simply open an issue with the tag "enhancement".
Don't forget to give the project a star! Thanks again!

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Top contributors:

<a href="https://github.com/Miragon/camunda-testing-examples/graphs/contributors">
  <img src="https://contrib.rocks/image?repo=Miragon/camunda-testing-examples" />
</a>

Made with [contrib.rocks](https://contrib.rocks).

<p style="text-align: right">(<a href="#readme-top">back to top</a>)</p>



<!-- LICENSE -->

## License

Distributed under the Apache License Version 2.0. See `LICENSE` for more information.

<p style="text-align: right">(<a href="#readme-top">back to top</a>)</p>


<!-- CONTACT -->

## Contact

Project
Link: [https://github.com/dominikhorn63/camunda-testing-examples](https://github.com/Miragon/camunda-testing-examples)

<p style="text-align: right">(<a href="#readme-top">back to top</a>)</p>


<!-- ACKNOWLEDGMENTS -->

## Acknowledgments

* [emaarco's bpmn-to-code plugin](https://github.com/emaarco/bpmn-to-code)

<p style="text-align: right">(<a href="#readme-top">back to top</a>)</p>

<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->

[contributors-shield]: https://img.shields.io/github/contributors?style=for-the-badge

[contributors-url]: https://github.com/Miragon/camunda-testing-examples/graphs/contributors

[forks-shield]: https://img.shields.io/github/forks?style=for-the-badge

[forks-url]: https://github.com/Miragon/camunda-testing-examples/fork

[stars-shield]: https://img.shields.io/github/stars?style=for-the-badge

[stars-url]: https://github.com/Miragon/camunda-testing-examples/stargazers

[issues-shield]: https://img.shields.io/github/issues?style=for-the-badge

[issues-url]: https://github.com/Miragon/camunda-testing-examples/issues

[license-shield]: https://img.shields.io/github/license?style=for-the-badge

[license-url]: https://github.com/Miragon/camunda-testing-examples/tree/master/LICENSE

[linkedin-shield]: https://img.shields.io/badge/LinkedIn-green?style=for-the-badge&logo=linkedin

[linkedin-url]: https://www.linkedin.com/in/dominikhorn

[JUnit-shield]: https://img.shields.io/badge/junit5-blue?style=for-the-badge&logo=junit5&logoColor=white

[JUnit-url]: https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-api

[Camunda-shield-test-coverage]: https://img.shields.io/badge/camunda_test_coverage-red?style=for-the-badge&logo=camunda&logoColor=white

[Camunda7-test-coverage-url]:https://mvnrepository.com/artifact/org.camunda.community.process_test_coverage/camunda-process-test-coverage-engine-platform-7

[Camunda-shield-extension]: https://img.shields.io/badge/camunda-extensions-yellow?style=for-the-badge&logo=camunda&logoColor=white

[Camunda-extension-url]: https://mvnrepository.com/search?q=org.camunda.bpm.extension


