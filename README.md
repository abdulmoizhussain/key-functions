# Key Functions (A better copy of [make-it-searchable](https://github.com/abdulmoizhussain/make-it-searchable))
### Removes all special characters from the text in Clipboard (which makes it google searchable) and saves it back into Clipboard.

## Executables are in: [Executables/](https://github.com/abdulmoizhussain/key-functions/tree/master/Executables)

### Antiviruses may detect it as a virus, because it is developed with a [key-logger (recorder)](https://en.wikipedia.org/wiki/Keystroke_logging) mechanism but surely does not log (record) any key, so you can whitelist it.

## Usage:
### Execute ***`key-functions.exe`*** OR ***`key-functions.jar`***

### `Set Cursor position (0, 0) while typing`: ###
* Adding ***hide mouse-pointer while typing*** is a tricky functionality, that's why setting cursor's position to (0, 0) coordinates while typing.

### `Clean special characters from clipboard`: ###
* E.g. text: `https://github.com/key-functions/edit/master/README.md`
* Prepend the text with "`mis `" like below and select it:
* !["mis https://github.com/abdulmoizhussain/key-functions"](https://github.com/abdulmoizhussain/key-functions/blob/master/example.png)
* To make the text easily searchable, copy it from the keyboard shortcut `ctrl + c`:
* Now paste it somewhere and it should be formatted like this: `https github com key functions edit master README md`

&nbsp;
### Java Library used: https://github.com/kwhat/jnativehook
### Single executable is generated by using: http://ninjacave.com/jarsplice
### `jarsplice` sources are:
* https://twitter.com/sfakher9612
* https://www.youtube.com/watch?v=YqGUk84BmlQ&feature=youtu.be