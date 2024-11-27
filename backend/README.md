### Install libs

```bash
npm install -g firebase-tools
```

### Test in a local emulator of firebase:
Best to do this to avoid costing compute credit by running on cloud
```bash
firebase init emulators
```
Choose firestore and functions
```bash
firebase emulators:start
```

### Deploy
```bash
firebase deploy --only functions
```