import { Component, OnInit } from '@angular/core';

@Component({
  // app-header tenho ele como header no app.component.html
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}
