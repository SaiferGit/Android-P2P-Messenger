# ChitChat P2P Messeging App

| About Our App |
| --------------- |
| <b> ChitChat </b> is a Network Layer messenger application that runs on Android environment. It supports only p2p data exchanging, built using Native Android. So Android phone is needed to run this app. Clients are connected through their IP address and port number with the devices under the same local network.|



### Features So Far:


##### Base Features
- IP address and port number login
- A beautiful UI for exchanged data
- Sharing textual files
- Save the exchanged messages whenever anyone wants
- Changing background option which is visible to both peers


##### Extra Features:
- Exchanging messages using voice
- Caesar Cipher encryption & decryption of data for security
- Delete chat history for individual and both the peers
- Splash screen
- Confirmation using alert dialogue in each step and Time of saving chat history
- get own network IP
- Own App Icon with own  designed app logo
- Added chat disconnect option
- text selectable, can be copied, pasted, shared  
- In total 10 types of UI for conversations


|  Why my app is correct? |
| --------------------------------- |
| We have implemented all the basic requirements that a network layer messenger application should contain. We have added a <b> bunch of extra features </b> that is mentioned above. <b> Used too simple logic </b> and stored <b> well documentation </b> for understanding what is going on through the whole code. We have <b> implemented the whole thing using java socket </b> which is the foundation level requirements. The program works so perfectly with the <b> magical GUI. </b> |


|  Particular Part |
| --------------------------------- |
| <b> <u> Tazkia did</u>: </b>  BG change, saving conversations, splash screen, app icon design, app logo design, designing beautiful UI and magical GUI, background picture selection, chat disconnect option, text selectable, can be copied, pasted, shared,removing chat history, app running, testing. |
| <b> <u> Saif did</u>:  </b>  Socket desiging, file sending, exchanging messages, Caesar Cipher encryption system, exchanged messages using voice, designing beautiful UI and magical GUI, alert dialogue confirmation and timing,removing chat history, app testing, debugging |

## How to run this app? 
### [More detail of our app can be found [here](https://github.com/SaiferGit/p2p-messenger/chitChat.apk)]
- Take two separate <b> Android </b> phones (API 19 or higher). Our App will run in the following Android versions:

- Open the <b> APK </b> that named <b> ChitChat.apk </b>, you can find it on the following  location: [APK Link](https://github.com/SaiferGit/p2p-messenger/blob/master/chitchat.apk). You can also run the app just like other android app by cloning from GitHub.
<br/>
- Once you run the app you can see the <b> Splash Screen </b>.
<br/>
![photo_2019-10-12_09-55-36](https://user-images.githubusercontent.com/44334473/66694572-9c3e5d00-ecd6-11e9-96c3-b2ecad5aad07.jpg)
<br/>
- You need to put value every field except <b>Encryption Key </b> field. But if you put the value of encryption key, then encrypted message will be send to your pair. So, your pair will also need to put the same encryption key for getting exact data for communication.
<br/>
![picture2](https://user-images.githubusercontent.com/44334473/66694636-5df56d80-ecd7-11e9-80ed-e6399b3a5dea.jpg)
<br/>
- You can get your IP by pressing <b> Get IP button. </b>
<br/>
![picture3(get ip)](https://user-images.githubusercontent.com/44334473/66694649-9301c000-ecd7-11e9-8bf7-fb31a6191d15.jpg)
<br/>

- After giving appropriate value to <b> Receive Port(Listen Port), Target IP, Target Port(Sending Port) and Encryption Key </b> depending on your value( on both the devices) , you will be redirected to a beautiful Chat Screen for exchanging your data. 
<br/>

![picture4(input of device 1)](https://user-images.githubusercontent.com/44334473/66694667-c3e1f500-ecd7-11e9-9279-d7a46c6c068f.jpg)
<br/>
![picture5(input of device 2)](https://user-images.githubusercontent.com/44334473/66694668-c5abb880-ecd7-11e9-8af2-14242dd94a23.jpg)
<br/>
<br/>
![picture6(chatbox)](https://user-images.githubusercontent.com/44334473/66694676-e3791d80-ecd7-11e9-8651-d2cf99a4beab.jpg)
<br/>
-  In <b> type your message here.. </b> you can write your messages what you wanna send to your pair(pictures from both end).
<br/>
![picture7(send file and msg reciever)](https://user-images.githubusercontent.com/44334473/66694723-7023db80-ecd8-11e9-9dbb-e4710eabba36.jpg)
<br/>

![picture8(send file and message sender)](https://user-images.githubusercontent.com/44334473/66694727-7b770700-ecd8-11e9-965f-1b77979c72e1.jpg)
<br/>
- In bottom left corner there has an attachment button which give you some more features such as <b> sending text files </b> and <b> exchange voice messages </b>.
<br/>
![picture9(file or voice)](https://user-images.githubusercontent.com/44334473/66694729-816ce800-ecd8-11e9-9e57-3e9ce0ac450c.jpg)
<br/>
![picture11(onlytextfile)](https://user-images.githubusercontent.com/44334473/66694751-cdb82800-ecd8-11e9-9581-9878eb871d10.jpg)
<br/>
![picture12(sure to snd file)](https://user-images.githubusercontent.com/44334473/66694766-eaecf680-ecd8-11e9-80af-c291ed9aa9db.jpg)
<br/>
- Exchanging voice messages:
<br/>
![picture13(Onvoicesendbutton)](https://user-images.githubusercontent.com/44334473/66694779-1243c380-ecd9-11e9-9596-7b03c46ae728.jpg)
<br/>
- Speech to text in socket application:
<br/>
![picture14(Speechtotext)](https://user-images.githubusercontent.com/44334473/66694792-3a332700-ecd9-11e9-9c38-d951fb341fc0.jpg)
![picture15(voice_msg_sent)](https://user-images.githubusercontent.com/44334473/66694797-40c19e80-ecd9-11e9-9a60-fbd5430b2784.jpg)
<br/>

- File will be send and downloaded on the following location: <b> android/data/com.example.p2p/... </b>
<br/>
![pictuure18(chat history saved)](https://user-images.githubusercontent.com/44334473/66694872-61d6bf00-ecda-11e9-9051-15e471442dc2.jpg)
<br/>
- In the top right corner there has three menu option for <b> saving conversations, changing background and disconnect your chat with your current pair </b>.
</br>
<br/>
![photo_2019-10-12_10-24-55](https://user-images.githubusercontent.com/44334473/66694888-95194e00-ecda-11e9-9d75-d9a284a82e14.jpg)
<br/>
- <b> Saving conversations </b> generates a text file named as <b> Chat History(current Time).txt </b> 
<br/>
![picture17(saving convo)](https://user-images.githubusercontent.com/44334473/66694805-6c448900-ecd9-11e9-8acc-176efd108266.jpg)

<br/>
![picture19(chathistory txt)](https://user-images.githubusercontent.com/44334473/66694806-6fd81000-ecd9-11e9-84f7-d5db60b39cde.jpg)
- <b>Changing Background </b> has <b> 5 additional layer </b>(layer3 as default) by which you can change your chat background that is <b> also visible to your pair</b>.
<br/>
![picture20(background_layouts)](https://user-images.githubusercontent.com/44334473/66694830-c2193100-ecd9-11e9-980c-a1760f6ffa35.jpg)
<br/>
![picture21(Blue bg example)](https://user-images.githubusercontent.com/44334473/66694831-c47b8b00-ecd9-11e9-8085-f7ba569374d9.jpg)
<br/>
- <b>Disconncet button </b> disconnects you from your chat and disconnects both of your listening a and sending socket. 
<br/>
![picture22(on disconnect button)](https://user-images.githubusercontent.com/44334473/66694837-ea089480-ecd9-11e9-8d3d-c2cb0062ea88.jpg)
<br/>
![picture23(disconnected)](https://user-images.githubusercontent.com/44334473/66694838-ee34b200-ecd9-11e9-9f62-4caca70a0ba2.jpg)
<br/>
- Can't access menu items without logging in.
<br/>
![picture24(cant_access_menu_item)](https://user-images.githubusercontent.com/44334473/66694848-220fd780-ecda-11e9-8812-e078033461ec.jpg)

<br/>
- Trying to connect with different encryption key results failure to get actual data: 
<br/>
![picture25(Encryption key 5)](https://user-images.githubusercontent.com/44334473/66694854-37850180-ecda-11e9-904a-ef2a5760ed6d.jpg)
<br/>
![picture26(en_key 15)](https://user-images.githubusercontent.com/44334473/66694855-394ec500-ecda-11e9-95f6-1965b9c35042.jpg)
<br/>
![picture27(cant decrypt msg)](https://user-images.githubusercontent.com/44334473/66694856-3c49b580-ecda-11e9-89d3-1e3e0c2ba58d.jpg)
<br/>
![picture28(cant dycrpt msg)](https://user-images.githubusercontent.com/44334473/66694858-3e137900-ecda-11e9-93e0-042cf377ef26.jpg)

## How to run this app? 
### [More detail of our app can be found [here](https://github.com/SaiferGit/p2p-messenger/chitChat.apk)]
- Take two separate <b> Android </b> phones (API 19 or higher). Our App will run in the following Android versions:

- Open the <b> APK </b> that named <b> ChitChat.apk </b>, you can find it on the following  location: [APK Link](https://github.com/SaiferGit/p2p-messenger/blob/master/chitchat.apk). You can also run the app just like other android app by cloning from GitHub.
<br/>
- Once you run the app you can see the <b> Splash Screen </b>.
<br/>
![photo_2019-10-12_09-55-36](https://user-images.githubusercontent.com/44334473/66694572-9c3e5d00-ecd6-11e9-96c3-b2ecad5aad07.jpg)
<br/>
- You need to put value every field except <b>Encryption Key </b> field. But if you put the value of encryption key, then encrypted message will be send to your pair. So, your pair will also need to put the same encryption key for getting exact data for communication.
<br/>
![picture2](https://user-images.githubusercontent.com/44334473/66694636-5df56d80-ecd7-11e9-80ed-e6399b3a5dea.jpg)
<br/>
- You can get your IP by pressing <b> Get IP button. </b>
<br/>
![picture3(get ip)](https://user-images.githubusercontent.com/44334473/66694649-9301c000-ecd7-11e9-8bf7-fb31a6191d15.jpg)
<br/>

- After giving appropriate value to <b> Receive Port(Listen Port), Target IP, Target Port(Sending Port) and Encryption Key </b> depending on your value( on both the devices) , you will be redirected to a beautiful Chat Screen for exchanging your data. 
<br/>

![picture4(input of device 1)](https://user-images.githubusercontent.com/44334473/66694667-c3e1f500-ecd7-11e9-9279-d7a46c6c068f.jpg)
<br/>
![picture5(input of device 2)](https://user-images.githubusercontent.com/44334473/66694668-c5abb880-ecd7-11e9-8af2-14242dd94a23.jpg)
<br/>
<br/>
![picture6(chatbox)](https://user-images.githubusercontent.com/44334473/66694676-e3791d80-ecd7-11e9-8651-d2cf99a4beab.jpg)
<br/>
-  In <b> type your message here.. </b> you can write your messages what you wanna send to your pair(pictures from both end).
<br/>
![picture7(send file and msg reciever)](https://user-images.githubusercontent.com/44334473/66694723-7023db80-ecd8-11e9-9dbb-e4710eabba36.jpg)
<br/>

![picture8(send file and message sender)](https://user-images.githubusercontent.com/44334473/66694727-7b770700-ecd8-11e9-965f-1b77979c72e1.jpg)
<br/>
- In bottom left corner there has an attachment button which give you some more features such as <b> sending text files </b> and <b> exchange voice messages </b>.
<br/>
![picture9(file or voice)](https://user-images.githubusercontent.com/44334473/66694729-816ce800-ecd8-11e9-9e57-3e9ce0ac450c.jpg)
<br/>
![picture11(onlytextfile)](https://user-images.githubusercontent.com/44334473/66694751-cdb82800-ecd8-11e9-9581-9878eb871d10.jpg)
<br/>
![picture12(sure to snd file)](https://user-images.githubusercontent.com/44334473/66694766-eaecf680-ecd8-11e9-80af-c291ed9aa9db.jpg)
<br/>
- Exchanging voice messages:
<br/>
![picture13(Onvoicesendbutton)](https://user-images.githubusercontent.com/44334473/66694779-1243c380-ecd9-11e9-9596-7b03c46ae728.jpg)
<br/>
- Speech to text in socket application:
<br/>
![picture14(Speechtotext)](https://user-images.githubusercontent.com/44334473/66694792-3a332700-ecd9-11e9-9c38-d951fb341fc0.jpg)
![picture15(voice_msg_sent)](https://user-images.githubusercontent.com/44334473/66694797-40c19e80-ecd9-11e9-9a60-fbd5430b2784.jpg)
<br/>

- File will be send and downloaded on the following location: <b> android/data/com.example.p2p/... </b>
<br/>
![pictuure18(chat history saved)](https://user-images.githubusercontent.com/44334473/66694872-61d6bf00-ecda-11e9-9051-15e471442dc2.jpg)
<br/>
- In the top right corner there has three menu option for <b> saving conversations, changing background and disconnect your chat with your current pair </b>.
</br>
<br/>
![photo_2019-10-12_10-24-55](https://user-images.githubusercontent.com/44334473/66694888-95194e00-ecda-11e9-9d75-d9a284a82e14.jpg)
<br/>
- <b> Saving conversations </b> generates a text file named as <b> Chat History(current Time).txt </b> 
<br/>
![picture17(saving convo)](https://user-images.githubusercontent.com/44334473/66694805-6c448900-ecd9-11e9-8acc-176efd108266.jpg)

<br/>
![picture19(chathistory txt)](https://user-images.githubusercontent.com/44334473/66694806-6fd81000-ecd9-11e9-84f7-d5db60b39cde.jpg)
- <b>Changing Background </b> has <b> 5 additional layer </b>(layer3 as default) by which you can change your chat background that is <b> also visible to your pair</b>.
<br/>
![picture20(background_layouts)](https://user-images.githubusercontent.com/44334473/66694830-c2193100-ecd9-11e9-980c-a1760f6ffa35.jpg)
<br/>
![picture21(Blue bg example)](https://user-images.githubusercontent.com/44334473/66694831-c47b8b00-ecd9-11e9-8085-f7ba569374d9.jpg)
<br/>
- <b>Disconncet button </b> disconnects you from your chat and disconnects both of your listening a and sending socket. 
<br/>
![picture22(on disconnect button)](https://user-images.githubusercontent.com/44334473/66694837-ea089480-ecd9-11e9-8d3d-c2cb0062ea88.jpg)
<br/>
![picture23(disconnected)](https://user-images.githubusercontent.com/44334473/66694838-ee34b200-ecd9-11e9-9f62-4caca70a0ba2.jpg)
<br/>
- Can't access menu items without logging in.
<br/>
![picture24(cant_access_menu_item)](https://user-images.githubusercontent.com/44334473/66694848-220fd780-ecda-11e9-8812-e078033461ec.jpg)

<br/>
- Trying to connect with different encryption key results failure to get actual data: 
<br/>
![picture25(Encryption key 5)](https://user-images.githubusercontent.com/44334473/66694854-37850180-ecda-11e9-904a-ef2a5760ed6d.jpg)
<br/>
![picture26(en_key 15)](https://user-images.githubusercontent.com/44334473/66694855-394ec500-ecda-11e9-95f6-1965b9c35042.jpg)
<br/>
![picture27(cant decrypt msg)](https://user-images.githubusercontent.com/44334473/66694856-3c49b580-ecda-11e9-89d3-1e3e0c2ba58d.jpg)
<br/>
![picture28(cant dycrpt msg)](https://user-images.githubusercontent.com/44334473/66694858-3e137900-ecda-11e9-93e0-042cf377ef26.jpg)
