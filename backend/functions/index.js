// based off: https://firebase.google.com/docs/functions/get-started?gen=2nd#node.js_2

// The Cloud Functions for Firebase SDK to create Cloud Functions and triggers.
const { onRequest } = require("firebase-functions/v2/https");
const { getMessaging } = require("firebase-admin/messaging");
// The Firebase Admin SDK to access Firestore.
const { initializeApp } = require("firebase-admin/app");
const { getFirestore } = require("firebase-admin/firestore");
const { log } = require("firebase-functions/logger");

initializeApp();
const messaging = getMessaging();

// from: https://firebase.google.com/docs/functions/use-cases#notify_users_when_something_interesting_happens
/**
 * Triggers when given a HTTP request and sends a notification.
 */
exports.sendNotification = onRequest(async (req, res) => {
  if (req.method !== "POST") {
    res.status(405).send("Only POST requests allowed!");
    return;
  }
  const jsonData = req.body;
  log("Received data:", jsonData);

  const notificationTokens = jsonData.tokens;
  if (notificationTokens === undefined || notificationTokens === null) {
    log("There is no tokens in the request.");
    res.status(400).status("There is no tokens in the request.");
    return;
  }
  // TODO: Check formatting of notification and data
  const notification = jsonData.notification;
  if (notification === undefined || notification === null) {
    log("There is no notification in the request.");
    res.status(400).status("There is no notification in the request.");
    return;
  }

  // const data = jsonData.data;
  // if (data === undefined || data === null) {
  //   log("There is no data in the request.");
  //   res.status(400).status("There is no data in the request.");
  //   return;
  // }

  log(
    `There are ${notificationTokens.length} tokens` +
      " to send notifications to."
  );

  // Send notifications to all tokens.
  const messages = [];
  notificationTokens.forEach((token) => {
    messages.push({
      token,
      notification,
      // data,
    });
  });
  
  log(`Sample message: ${JSON.stringify(messages[0])}`);

  const batchResponse = await messaging.sendEach(messages);

  if (batchResponse.failureCount < 1) {
    // Messages sent sucessfully. We're done!
    log("Messages sent.");
    res.json({ result: `Messages Sent` });
    return;
  }
  warn(`${batchResponse.failureCount} messages weren't sent.`, batchResponse);

  // Clean up the tokens that are not registered any more.
  for (let i = 0; i < batchResponse.responses.length; i++) {
    const errorCode =
      batchResponse.responses[i].error !== null
        ? batchResponse.responses[i].error.code
        : null;
    const errorMessage =
      batchResponse.responses[i].error !== null
        ? batchResponse.responses[i].error.message
        : null;
    if (
      errorCode === "messaging/invalid-registration-token" ||
      errorCode === "messaging/registration-token-not-registered" ||
      (errorCode === "messaging/invalid-argument" &&
        errorMessage ===
          "The registration token is not a valid FCM registration token")
    ) {
      log(`Removing invalid token: ${messages[i].token}`);
      await tokensRef.child(messages[i].token).remove();
    }
  }

  // Send back a message that we've successfully written the message
  res.json({ result: `Messages sent, but invalid tokens where cleaned up` });
});
