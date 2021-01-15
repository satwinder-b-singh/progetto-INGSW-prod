import { Component, OnInit } from '@angular/core';
import { ApiService } from './Service/api.service';
import { Router } from '@angular/router';
import {User} from './Model/user' ;
const bar: User= { email:"visitator@gmail.com" , username: "visitator", password:"vistaws", usertype: "visitator", age:"21"};

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  constructor(private auth: ApiService, private router: Router) {

  }
  ngOnInit() {
    if (this.auth.isAuthenticated != null) {
      if (this.auth.isAuthenticated && this.auth.getAuthType() == "customer") {
        this.router.navigate(["/home"]);
      } else if (this.auth.isAuthenticated && this.auth.getAuthType() == "admin") {
        this.router.navigate(["/admin"]);
      }
    } //else if (this.auth.isAuthenticated == null) {
      //this.router.navigate(["/login"]);
    //}
    else if (this.auth.isAuthenticated == null) {
      this.auth.userLogin(bar).
      subscribe(res => {
        if (res.status == "200") {
          console.log("PUTTANA")
          this.auth.storeToken(res.auth_TOKEN, "visitator");
          this.router.navigate(['/home']);
        }
        else if (res.status == "500") {
          console.log("PUTTANA")
        }
      }),
        console.log("PUTTANA")
      this.router.navigate(["/home"]);
    }
  }
}
