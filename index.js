'use strict'

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.sendNotification = functions.database.ref('/notifications/{user_id}/{notification_id}').onWrite((data,context) =>
{
  const user_id = context.params.user_id;
  const notification_id = context.params.notification_id;

  console.log('The user id is:', user_id);
  if(!data.after.val())
  {
    console.log('Notification has been deleted', notification_id);

  }
  const fromUser = admin.database().ref(`/notifications/${user_id}/${notification_id}`).once('value');
  return fromUser.then(fromUserResult => {
    const from_user_id = fromUserResult.val().from;

    console.log('You have a new notification',from_user_id);

    const userQuery = admin.database().ref(`/Users/${from_user_id}/name`).once('value');
    return userQuery.then(userResult =>{
      const userName = userResult.val();
      const deviceToken = admin.database().ref(`/Users/${user_id}/device_token`).once('value');
      return deviceToken.then(result => {
        const token_id = result.val();
        const payload =
        {
          notification:{
            title: "New Request",
            body: `${userName} has sent a new request`,
            icon: "default",
            click_action: "com.example.chatapp_TARGET_NOTIFICATION"
          },
          data:{
            from_user_id : from_user_id
          }
        };
        return admin.messaging().sendToDevice(token_id,payload).then(response => {
          console.log('This was a notification feature');
          return true;
        });
      });


    });



  });


});

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });
