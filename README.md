# Bill Smart

## Setup

```shell
chmod +x .githooks/pre-commit
git config --get core.hooksPath
``` 

Then download `google-services.json` from Firebase and place it in `app/` directory.

Setup `.firebaserc` file with the following content:

```json
{
  "projects": {
    "default": // Firebase project name
  }
}
```

## Locally Run Firebase Emulators

```shell
cd functions && npm run serve
```

## Code Style References

- [tivi's configuration](https://github.com/chrisbanes/tivi/tree/main?tab=readme-ov-file#code-style)
- [Duckduckgo's configuration](https://github.com/duckduckgo/Android/blob/develop/.githooks/pre-commit)
