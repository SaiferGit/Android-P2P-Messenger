
		************************************************************************************
		************************************************************************************
		*********     CSE 362 Project: P2P messaging System on Android platform    *********
		*********     Project completed by                                         *********
		*********     Tajkia Nuri Ananna - 2016331092                              *********
		*********     Munshi Saifuzzaman - 2016331094                              *********
		************************************************************************************ 
		************************************************************************************
		

		[ This documentation is written in notepad just to follow the instructions you gave us
		  to write a readme.txt file. So, please open it in notepad instead of any other text editor]
		____________________________________________________________________________________
		|>>>>>>>>>>>>>>>>>>>                About our App               <<<<<<<<<<<<<<<<<<<|
		|__________________________________________________________________________________| 
		|	ChitChat is a Network Layer messenger application that runs on Android     |
		| environment. It supports only p2p data exchanging, built using Native Android.   |
		| So Android phone is needed to run this app.                                      |
		|__________________________________________________________________________________|



		_____________________________________________________________________________________
		|>>>>>>>>>>>>>                  Features Implemented                 <<<<<<<<<<<<<<<|
		|___________________________________________________________________________________|
		|          Basic Features                |               Extra Features             |
		|________________________________________|__________________________________________|
		|     IP address and port number login   |      Exchanged messages using voice      |
		|----------------------------------------|------------------------------------------|
		|    A beautiful UI for exchanged data   |   Caesar Cipher encryption & decryption  |
		|                                        |         of data for security             |
		|----------------------------------------|------------------------------------------|
		|          Sharing textual files         | Exchanging messages time added, time	    | 
		|					 | added to chat history		    |
		|----------------------------------------|------------------------------------------|
		|     Save the exchanged messages        | Voice Mode for sending messages, giving  |
		|     whenever anyone wants		 |all the commands such as changing         |
		|					 |background and sharing file also	    |
		|----------------------------------------|------------------------------------------|
		|  Changing background option which is   |    Delete chat history for individual and|
		|        visible to both peers.          | both the peers                           |
		|----------------------------------------|------------------------------------------|
		|					 |      Added chat disconnect option        |
		|----------------------------------------|------------------------------------------|
		|                                        |    Confirmation using alert dialogue in  |
        	|                                        | each step and Time of saving chat history|	    	 
		|----------------------------------------|------------------------------------------|
		|					 |   Own App Icon with own  designed app    |
		|					 |logo, Splash Screen			    |
		|----------------------------------------|------------------------------------------|
        	|                                        | In total 10 types of UI for conversations|   	        
		|----------------------------------------|------------------------------------------|
		|					 |  text selectable, can be copied, pasted, |
		|					 |  shared  				    |
		|----------------------------------------|------------------------------------------|
		|					 |  get own network IP			    |
		|----------------------------------------|------------------------------------------|
		|					 |  reset layout as default 		    |
		|________________________________________|__________________________________________|
	

	        [ A demo example is given here(showing how to run and how the app works) as: 
		  ChitChat.mp4. you can find it on the main directory of our zip, just where you find 
		  this readme.txt]
		_____________________________________________________________________________________
		|>>>>>>>>>>>>>>>>>>>>             How to run this app?         <<<<<<<<<<<<<<<<<<<<<|
		|_______|___________________________________________________________________________|                  
		|    1. |   Take two separate Android phones (API 19 or higher).                    |
		|-------|---------------------------------------------------------------------------|
		|    2. |   Open the APK that named ChitChat.apk in your taken androids(use data    |
		|       | cable to take it in your phone and install it), you can find it on the    |
		|       | following  location: 	(just where you find this readme.txt)		    |
		|	|     https://github.com/SaiferGit/p2p-messenger/blob/master/chitchat.apk   |
		|-------|---------------------------------------------------------------------------|
		|    3. |   Once you run the app you can see the following fields:                  |
		|-------|------|                                                              |-----|
		        |    i.|    Receive port is your listen port by which you can get data.     |
        		|------|--------------------------------------------------------------------|
        		|   ii.|    Start Server button is the button by which you can start your   |
	        	|      |listening port that is written in the Rec port. Once you start the  |
			|      |server you will see the following fields:			    |
	        	|------|---|							       |----|
	        	|  iii.|    Target IP is what your other phone’s IP is/your friends phone IP|
	        	|------|--------------------------------------------------------------------|
	        	|   iv.|    Target Port is what your other phone’s receiving port. By which | 
			|      |you can send your data on that port.                                |
			|------|--------------------------------------------------------------------|
			|  v.  |    Press Connect for connecting with your peer.It will redirect you| 
			|      |to your chat screen.                                                |
			|------|--------------------------------------------------------------------|
			|  vi. |    En Key is the encryption key which is used to encrypt exchanged | 
			|      |data for security purpose. En Key must be same for both the peer to |
		        |      |get actual exchanged data. It is a positive integer.                |
			|------|--------------------------------------------------------------------|
			|  vii.|    Get IP is the button from where you can get your IP.            |
		|-------|------|--------------------------------------------------------------------|
		|    4. |    In chat screen you can exchange messages, share text files and voice   | 
		|	|  messages with your peer.   					            |
		|-------|------|                                                             |------| 
		        |    i.|   In "type your message here" field you can type what you want to  |
		        |      |send to your peer.                                                  |
		        |------|--------------------------------------------------------------------|
		        |   ii.|    In bottom left there has an attachment icon by which you can    | 
		        |      |either share any text file from your storage or you can share any   |
		        |      |voice message you want. Your shared file will be automatically      | 
			|      |downloaded in your peer's following location:			    |
			|      |               android/data/com.example.p2pmessenger/files...       |
			|------|--------------------------------------------------------------------|
			|  iii.|    Through voice message we used English Language Model, so you can| 
			|      |send voice message only in English.				    |
			|------|--------------------------------------------------------------------|
			|  iv. |    You can select any text message on long press, can copy, paste  | 
			|      |and share the selected text.  					    |
		|-------|------|--------------------------------------------------------------------| 
		|   5.  |   In top right corner you can see a menu bar. Tap the menu bar and it will|
		|       | pop up 6 options.                                                         |
		|-------|------|                                                             |------|
			|    i.|    By clicking Save Chat, you can save your exchanged messages.    |
        		|      |The chat will be saved on: 					    |
			|      |	android/data/com.example.p2pmessenger/files...		    |
        		|------|--------------------------------------------------------------------|
			|   ii.|    By clicking Change Background, you can change your chat         |
        		|      |background for both of you and your peer.                           |
        		|------|--------------------------------------------------------------------|
        		|  iii.|    By clicking Disconnect, you can disconnect your chat from your  |
			|      |peer and remove yourself from getting data.			    |
                        |------|--------------------------------------------------------------------|
  			|  iv. |    By clicking Remove All Chat, you can remove all the conversation|
 			|      |for yourself and as well as for the other peer.			    |
		 	|------|--------------------------------------------------------------------|
  			|  v.  |    By clicking reset layout, you can reset the background as defult|
 			|      |for both you and your peer.					    |
		 	|------|--------------------------------------------------------------------|
  			|  vi. |    By clicking Voice Mode: OFF, you can enable Voice Mode: ON and  |
 			|      |can commands for all the functionalities using specific voice       |
			|      |command.							    |
		|-------|------|--------------------------------------------------------------------|
		|_______|______|____________________________________________________________________|


	        [  ]
		_____________________________________________________________________________________
		|>>>>>>>>>>>>>>>>>>>>             How it works?	               <<<<<<<<<<<<<<<<<<<<<|
		|___________________________________________________________________________________|                  
		| First Activity  |     When the first activity is loaded we have to give  user's   |
		|	&	  |listening port first, which is known as start server from both   |
		| Chat Activity   |end. Other input initializes where our data will be send. So,    | 
		|		  |when everything goes okay connect button directs us to the chat  |
		|		  |activity where we can exchange all of our data with our peers.   |
		|		  |In Bottom left corner attachment alows us to send file and send  |
		|		  |voice messages.						    |						
		|-----------------|-----------------------------------------------------------------|
		| Encryption key  |	Encryption key is needed to secure our exchanged data, you  |
		|		  |can give any integer value to it , but must be same for both. For|
		|		  |our data security we implemented Caesar Cipher encryption &      |
		|		  |decryption.							    |
		|-----------------|-----------------------------------------------------------------|
		| send files      |	directs us to the storage to select a text file, and when we|
		|		  |click on the send button it opens the file, read it line by line |
		|		  |and send it just like a string message by doing Caesar Cipher    |
		|		  |encryption and On the other end when the user receives it with   |
		|		  |decryption. A code is added before sending the file so that other|
		|		  |end understood  that it is a file. Then from other end we create |
		|		  |a text file in android/data/com.example.p2pmessenger/files and   |
		|		  |saves it line by line just like a simple text file.              |
		|-----------------|-----------------------------------------------------------------|
		| voice button	  |	when voice button is clicked a popup wants some voice       |
		|		  |message from us   and set the best results over the "type your   |
		|		  |message here..." edit text. then we can send it just like other  |
		|		  |messages with propar encryption. 				    |
		|-----------------|-----------------------------------------------------------------|
		| Save Chat       |	when save chat is clicked we open a dialog box to varify    |
		|		  |from user whether he really needs it or not. Once the user 	    |
		|		  |confirms, it calls writeToFile method where we created  a text   |
		|		  |file, write it line by line. Own messages are initialized with   |
		|		  |"ME" and other peer messages with "CLIENT".			    |
		|-----------------|-----------------------------------------------------------------|
		|Change Background|	When Change Background is clicked we gave users one default |
		|		  |and nine more great background themes for beautify his chat box. |
		|		  |Once the user choose a layout, it sends a encrypted code to other|
		|		  |peer, change background for all and notify all the user using a  |
		|		  |message.							    |
		|-----------------|-----------------------------------------------------------------|
		| Disconnect	  |	Here by disconnecting, a user can disconnect from his chat  |
		|		  |as his client and server socket get closed. Other user will still| 
		|		  |be connected to the port, can send messages but disconencted user|
		|		  |just can't receive it.					    |
		|-----------------|-----------------------------------------------------------------|
		|Remove All Chat  |	Here this gives two functionalities one by removing all the |
		|		  |chat from own window, just remove all the views programatically  |
		|		  |and the other by  removing all the chat for both window.	    |
		|-----------------|-----------------------------------------------------------------|
		|Reset Layout     |	Here by reseting layout, you can reset your layout(BG) as   |
		|		  |default for both window. just changed the background like others |
		|-----------------|-----------------------------------------------------------------|
		|Voice Mode:      |	Specific commands are given as voice input when the mode is |
		|	ON/OFF	  |enabled. A new voice icon is added when the mode is enabled, you |
		|		  |can exchange messages also through this command		    |
		|-----------------|-----------------------------------------------------------------|
		| Text Selection  |	We can select any text messages by on long press, can copy  |
		|		  |and paste to clipboard and share it to other application by 	    |
		|		  |implementing textAllSelectable.				    |
		|_________________|_________________________________________________________________|
		|_________________|_________________________________________________________________|

		_____________________________________________________________________________________
		|>>>>>>>>>>>>>>>>>>>              Why is it correct?               <<<<<<<<<<<<<<<<<|
		|___________________________________________________________________________________|
		|	We have implemented all the basic requirements that a network layer 	    |
		| messenger application should contain. We have added a bunch of extra features that|
		| is mentioned above.Used too simple logic and stored well documentation for	    | 
		| understanding what is going on through the whole code. We have implemented the    |
		| whole thing using java socket which is the foundation level requirements. The     |
		| program works so perfectly with the magical GUI. So we think our project is 	    |
		| correct.									    | 
		|___________________________________________________________________________________|

		_____________________________________________________________________________________
		|>>>>>>>>>>>>>>>>>>>              Particular Part              	   <<<<<<<<<<<<<<<<<|
		|___________|_______________________________________________________________________|
		|Tajkia did:| BG change, saving conversations, splash screen, app icon design, 	    |
		|           | app logo design, designing beautiful UI and magical GUI, background   |
		|	    | picture selection, chat disconnect option, text selectable, can be    | 
		|	    | copied, pasted, shared,removing chat history, reset layout voice      |
		|	    | enable mood, chat history time, app running, testing.		    |
		|-----------|-----------------------------------------------------------------------|
		|Saif did:  | Socket desiging, file sending, exchanging messages, Caesar Cipher     |
		|           | encryption system, exchanged messages using voice, designing beautiful| 
		|	    | UI and magical GUI, alert dialogue confirmation and timing,removing   |
    	        |           | chat history, voice enable mood, exchanged message time, 		    |
		|	    | app testing, debugging.						    |
            	|___________|_______________________________________________________________________|

		>> Later added in saterday final submission:
		  	1. Menu Item: voice mode 
		  	2. sending and receiving end message exchanging time added.
		  	3. chat histry with exchanged messages time.
		  	4. Layout design updated.
			5. Reset Layout
  

   		