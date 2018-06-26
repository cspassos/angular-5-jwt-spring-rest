import { Injectable, EventEmitter } from '@angular/core';
import { User } from '../model/user.model';

@Injectable({
  providedIn: 'root'
})
export class SharedService {

  public static instance: SharedService = null;
  user: User;
  token: string;
  //quando estiver logado vai aparecer os menus e demais funcionalidades,
  //quando nao estiver so vai aparecer a tela de login
  showTemplate = new EventEmitter<boolean>();

  constructor() {
    return SharedService.instance = SharedService.instance || this;
   }

   public static getInstance(){
     if(this.instance == null){
       this.instance = new SharedService();
     }
     return this.instance;
   }

   isLoggeIn():boolean {
    if(this.user == null){
      return false;
    }
    return this.user.email != '';
   }

}
