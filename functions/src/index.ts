/* eslint-disable max-len */
import {HttpsError, onCall} from "firebase-functions/v2/https";
import {initializeApp} from "firebase-admin/app";
import {auth, credential, firestore} from "firebase-admin";
import {CollectionNames} from "./collectionNames";


initializeApp({
  // eslint-disable-next-line @typescript-eslint/no-var-requires
  credential: credential.cert(require("../serviceAccount.json")),
  databaseURL: "https://bill-smart-ada54.firebase.io",
});

export const updateUserDisplayName = onCall((request) => {
  const {displayName} = request.data;
  return auth().updateUser(request.auth?.uid || "", {
    displayName: displayName,
  })
    .then(() => {
      return true;
    })
    .catch((error) => {
      throw new HttpsError("unknown", error.message, error);
    });
});

export const addNewFriendByEmail = onCall((request) => {
  const currentUserUID = request.auth?.token.uid || "";
  const {email} = request.data;

  return auth().getUserByEmail(email)
    .then((userRecord) => {
      const friendUID = userRecord.uid;
      if (currentUserUID === friendUID) {
        throw new HttpsError("invalid-argument", "You can't add yourself as a friend!");
      }

      return firestore().collection(CollectionNames.USERS).doc(currentUserUID)
        .collection(CollectionNames.FRIENDS).doc(friendUID).get()
        .then((doc) => {
          if (doc.exists) {
            throw new HttpsError("already-exists", "This user is already your friend!");
          }
        })
        .then(() => {
          return firestore().collection(CollectionNames.USERS).doc(currentUserUID)
            .collection(CollectionNames.FRIENDS).doc(friendUID).set({})
            .then(() => {
              return firestore().collection(CollectionNames.USERS).doc(friendUID)
                .collection(CollectionNames.FRIENDS).doc(currentUserUID).set({})
                .then(() => "New friend added!")
                .catch((error) => {
                  throw new HttpsError("unknown", error.message, error);
                });
            });
        });
    })
    .catch((error) => {
      throw new HttpsError("unknown", error.message, error);
    });
});


export const getFriendInfoByUID = onCall((request) => {
  const {uid} = request.data;
  return auth().getUser(uid)
    .then((userRecord) => {
      return {
        uid: userRecord.uid,
        displayName: userRecord.displayName || "",
        email: userRecord.email || "",
        photoURL: userRecord.photoURL || "",
      };
    })
    .catch((error) => {
      throw new HttpsError("unknown", error.message, error);
    });
});

export const getFriendsList = onCall((request) => {
  const currentUserUID = request.auth?.token.uid || "";
  return firestore().collection(CollectionNames.USERS).doc(currentUserUID)
    .collection(CollectionNames.FRIENDS).listDocuments()
    .then((docs) => {
      return Promise.all(docs.map((doc) => {
        return auth().getUser(doc.id).then((userRecord) => {
          return {
            uid: doc.id,
            displayName: userRecord.displayName || "",
            email: userRecord.email || "",
            photoURL: userRecord.photoURL || "",
          };
        });
      }));
    })
    .catch((error) => {
      throw new HttpsError("unknown", error.message, error);
    });
});
