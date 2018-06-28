import { CanActivate, Router } from '@angular/router';
import { SharedService } from './../../services/shared.service';
import { ActivatedRouteSnapshot } from '@angular/router';
import { RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';

@Injectable()
export class AuthGuard implements CanActivate {
    
    public shared : SharedService;
    
    constructor(private router: Router){
        this.shared = SharedService.getInstance();
    }

    canActivate(
        route: ActivatedRouteSnapshot, 
        state: RouterStateSnapshot):  Observable<boolean> | boolean {
        if(this.shared.isLoggeIn()){
            return true;
        }
        this.router.navigate(['/login']);
        return false;
    }
}
