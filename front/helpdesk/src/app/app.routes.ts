import { Routes, RouterModule } from '@angular/router';
import { ModuleWithProviders } from '@angular/core';

import { LoginComponent } from './components/security/login/login.component';
import { HomeComponent } from './components/home/home.component';
import { AuthGuard } from './components/security/auth.guard';
import { UserNewComponent } from './components/user-new/user-new.component';
import { UserListComponent } from './components/user-list/user-list.component';
import { TicketNewComponent } from './components/ticket-new/ticket-new.component';

export const ROUTES: Routes = [
    { path : '', component: HomeComponent, canActivate: [AuthGuard]}, //rota inicial
    { path : 'login', component: LoginComponent },
    { path : 'user-new', component: UserNewComponent, canActivate: [AuthGuard]},
    { path : 'user-new/:id', component: UserNewComponent, canActivate: [AuthGuard]},
    { path : 'user-list', component: UserListComponent, canActivate: [AuthGuard]},
    { path : 'ticket-new', component: TicketNewComponent, canActivate: [AuthGuard]},
]

export const routes: ModuleWithProviders = RouterModule.forRoot(ROUTES);