import { Component, OnInit } from '@angular/core';
import { SharedService } from '../../services/shared.service';
import { DialogService } from '../../dialog.service';
import { UserService } from '../../services/user.service';
import { Router } from '@angular/router';
import { ResponseApi } from '../../model/response-api';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {

  page: number = 0;
  count: number = 5;
  pages: Array<number>;
  shared : SharedService;
  message: {};
  classCss : {};
  listUser = [];

  constructor(
    private dialogService: DialogService,
    private userService: UserService,
    private router: Router
  ) { 
    this.shared = SharedService.getInstance();
   }

  ngOnInit() {
    this.findAll(this.page, this.count); // vai pegar a lista dos usuarios
  }

  findAll(page: number, count: number){
    this.userService.findAll(page, count).subscribe((responseApi:ResponseApi) => {
      this.listUser = responseApi['data']['content'];
      this.pages = new Array(responseApi['data']['totalPages']);
    }, err => {
        this.showMessage({
        type: 'error',
        text: err['error']['errors'][0]
      });
    })
  }

  edit(id:string){
    this.router.navigate(['/user-new', id]);
  }

  delete(id:string){
    this.dialogService.confirm(' Do you want to delete the user ?')
      .then((candelete:boolean) => {
        if(candelete){
          this.message = {};
          this.userService.delete(id).subscribe((responseApi:ResponseApi) => {
            this.showMessage({
              type: 'success',
              text: 'Record deleted'
            });
            this.findAll(this.page, this.count);
          }, err => {
              this.showMessage({
              type: 'error',
              text: err['error']['errors'][0]
          })
        }
      })
  }

  private showMessage(message: {type: string, text: string}) : void {
    this.message = message;
    this.buildClasses(message.type);
    setTimeout(() => {
      this.message = undefined;
    }, 3000);
  }

  private buildClasses(type: string): void {
    this.classCss = {
      'alert' : true
    }
    this.classCss['alert-'+type] = true
  }
}
