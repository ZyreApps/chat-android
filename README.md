# ZyreTestAndroid
Proof of concept chat app with [Zyre JNI bindings](https://github.com/zeromq/zyre/releases/tag/v1.1.0)

![](https://raw.githubusercontent.com/spro/ZyreTestAndroid/master/screenshot.png)

# Installation

With gradle, `./gradlew installDebug`

# Testing

Install [Pyre](https://github.com/zeromq/pyre) to use the Python testing scripts.

Watch Zyre messages on the network: `python scripts/listen.py`

Send SHOUT messages interactively: `python scripts/send.py myusername`

# License

Mozilla Public License 2, see LICENSE.
