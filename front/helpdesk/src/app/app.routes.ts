import { Routes, RouterModule } from '@angular/router';
import { ModuleWithProviders } from '@angular/core';

import { LoginComponent } from './components/security/login/login.component';
import { HomeComponent } from './components/home/home.component';
import { AuthGuard } from './components/security/auth.guard';

export const ROUTES: Routes = [
    { path : '', component: HomeComponent, canActivate: [AuthGuard]}, //rota inicial
    { path : 'login', component: LoginComponent }
]

export const routes: ModuleWithProviders = RouterModule.forRoot(ROUTES);