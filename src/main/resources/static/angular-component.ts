import { Component, ElementRef, OnDestroy, OnInit, ViewChild } from "@angular/core";

import * as Stomp from '@stomp/stompjs';
import * as SockJs from 'sockjs-client';

@Component({
  selector: 'app-socket-demo',
  templateUrl: './app-socket-demo.component.html',
  styleUrls: ['./app-socket-demo.component.css']
})
export class SocketDemoComponent implements OnInit, OnDestroy {

  stompClient: Stomp.Client;
  @ViewChild('socketResults') result: ElementRef<HTMLParagraphElement>;
  @ViewChild('socketName') input: ElementRef<HTMLInputElement>;

  ngOnInit(): void {
    this.connectSocket();
  }

  connectSocket() {
    // follow this instead: https://stomp-js.github.io/guide/stompjs/using-stompjs-v5.html
    this.stompClient = new Stomp.Client({
      brokerURL: 'ws://localhost:8080/up-guide-websocket',
      debug: (s) => console.log(s)
    });

    /**
     * with sockjs connection (if websocket not supported)
     * in spring boot, registry url should have same endpoint with and without sockJs
     * with is used when webSocketFactory is used and without is used if WebSocketFactory is commented out
     */
    this.stompClient.webSocketFactory = () => {
      return new SockJs('http://localhost:8080/up-guide-websocket');
    };

    this.stompClient.onConnect = (frame) => {
      console.log('connected: ', frame);
      this.stompClient.subscribe('/topic/greetings', (greeting) => {
          const greetMsg = JSON.parse(greeting.body).content;
          this.result.nativeElement.innerText += ' ' + greetMsg;
      });
    };
    this.stompClient.activate();
  }

  onSocketSubmit() {
    // get value
    const value = this.input.nativeElement.value;

    // send data
    this.stompClient.publish({
        destination: '/app/hello', 
        headers: {'Access-Control-Allow-Origin': '*'}, 
        body: JSON.stringify({'name': value})
    });

    // empty it
    this.input.nativeElement.value = '';
  }

  ngOnDestroy(): void {
    if (this.stompClient != null) {
      this.stompClient.deactivate();
    }
  }
}

// create an angular app with this in the ts file and same with corresponding html file
