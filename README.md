# YoutubeDL-UI

This is just a JavaFX UI project for the youtoube-dl exe.

![YoutubeDL-UI Screenshot](YoutubeDL-UI.png)

## Usage
To use the UI download the zipped release and unzip it.

Start the application with 

* `bin/YoutubeDL-UI-launcher` on linux
* `bin/YoutubeDL-UI-launcher.bat` on windows

## Configuration
As default the application expects the youtube-dl.exe in the start folder and the downloaded files are stored in the same folder.

It is possible to change the configuration by creating a config.json file in the same folder as application is started.

```json
{
  "outputPath": ".",
  "outputTemplate": "%(title)s-%(id)s.%(ext)s",
  "tools": [
    {
      "name": "youtube-dl",
      "path": "youtube-dl.exe"
    },
    {
      "name": "yt-dlp",
      "path": "yt-dlp.exe"
    }
  ]
}
```
If a config value is not set, the default is used. As the example shows, yt-dlp is also supported.

