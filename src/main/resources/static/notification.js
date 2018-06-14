async function init() {
    const registration = await navigator.serviceWorker.register('/sw.js');
    
    firebase.initializeApp({
      'messagingSenderId': '933567446829'
    });
    const messaging = firebase.messaging();
    
    messaging.useServiceWorker(registration);
    
    try {
        await messaging.requestPermission();
      } catch (e) {
        console.log('Unable to get permission', e);
        return;
      }

      navigator.serviceWorker.addEventListener('message', event => {
        if (event.data === 'newData') {
        }
      });
      
      const currentToken = await messaging.getToken();
      fetch('/register', { method: 'post', body: currentToken });

      messaging.onTokenRefresh(async () => {
        console.log('token refreshed');
        const newToken = await messaging.getToken();
        fetch('/register', { method: 'post', body: currentToken });
      });
}
init();