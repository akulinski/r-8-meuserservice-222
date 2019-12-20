import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { UserProfile } from 'app/shared/model/user-profile.model';
import { UserProfileService } from './user-profile.service';
import { UserProfileComponent } from './user-profile.component';
import { UserProfileDetailComponent } from './user-profile-detail.component';
import { UserProfileUpdateComponent } from './user-profile-update.component';
import { IUserProfile } from 'app/shared/model/user-profile.model';

@Injectable({ providedIn: 'root' })
export class UserProfileResolve implements Resolve<IUserProfile> {
  constructor(private service: UserProfileService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUserProfile> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(map((userProfile: HttpResponse<UserProfile>) => userProfile.body));
    }
    return of(new UserProfile());
  }
}

export const userProfileRoute: Routes = [
  {
    path: '',
    component: UserProfileComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'UserProfiles'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: UserProfileDetailComponent,
    resolve: {
      userProfile: UserProfileResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'UserProfiles'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: UserProfileUpdateComponent,
    resolve: {
      userProfile: UserProfileResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'UserProfiles'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: UserProfileUpdateComponent,
    resolve: {
      userProfile: UserProfileResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'UserProfiles'
    },
    canActivate: [UserRouteAccessService]
  }
];
