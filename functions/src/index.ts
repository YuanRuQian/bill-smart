import {HttpsError, onCall} from "firebase-functions/v2/https";
import * as logger from "firebase-functions/logger";
import {initializeApp} from "firebase-admin/app";
import {credential} from "firebase-admin";
import {firestore} from "firebase-admin"
import { CollectionNames } from "./collectionNames";

// Start writing functions
// https://firebase.google.com/docs/functions/typescript

initializeApp({
  // eslint-disable-next-line @typescript-eslint/no-var-requires
  credential: credential.cert(require("../serviceAccount.json")),
  databaseURL: "https://bill-smart-ada54.firebase.io",
});

export const addMessage = onCall((request) => {
  // [START_EXCLUDE]
  // [START v2readMessageData]
  // Message text passed from the client.
  const text = request.data.text;
  logger.info("Adding message", {text});
  // [END v2readMessageData]
  // [START v2messageHttpsErrors]
  // Checking attribute.
  if (!(typeof text === "string") || text.length === 0) {
    // Throwing an HttpsError so that the client gets the error details.
    throw new HttpsError("invalid-argument", "The function must be called " +
            "with one arguments \"text\" containing the message text to add.");
  }
  // Checking that the user is authenticated.
  if (!request.auth) {
    // Throwing an HttpsError so that the client gets the error details.
    throw new HttpsError("failed-precondition", "The function must be " +
            "called while authenticated.");
  }

  const uid = request.auth.uid;
  const name = request.auth.token.name || null;
  const picture = request.auth.token.picture || null;
  const email = request.auth.token.email || null;

  logger.info("Adding message by user: ", {text, uid, name, picture, email});

  return firestore().collection(CollectionNames.MESSAGES).add({
    text,
    author: {uid, name, picture, email},
  }).then(() => {
    logger.info("New Message written");
    // Returning the sanitized message to the client.
    return text;
  })
    // [END v2returnMessageAsync]
    .catch((error) => {
      // Re-throwing the error as an HttpsError so that the client gets
      // the error details.
      throw new HttpsError("unknown", error.message, error);
    });
  // [END_EXCLUDE]
});


export const addNewUserInfo = onCall((request) => {
  const {uid, displayName, email} = request.data;
  return firestore().collection(CollectionNames.USERS).doc(uid).set({
    displayName,
    email,
  })
  .then(() => {
    logger.info("New User Info written");
    return `Welcome, ${displayName}!`
  })
  .catch((error) => {
    throw new HttpsError("unknown", error.message, error);
  });
});