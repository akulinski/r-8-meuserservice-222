import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { FollowerXFollowed } from 'app/shared/model/follower-x-followed.model';
import { FollowerXFollowedService } from './follower-x-followed.service';
import { FollowerXFollowedComponent } from './follower-x-followed.component';
import { FollowerXFollowedDetailComponent } from './follower-x-followed-detail.component';
import { FollowerXFollowedUpdateComponent } from './follower-x-followed-update.component';
import { IFollowerXFollowed } from 'app/shared/model/follower-x-followed.model';

@Injectable({ providedIn: 'root' })
export class FollowerXFollowedResolve implements Resolve<IFollowerXFollowed> {
  constructor(private service: FollowerXFollowedService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFollowerXFollowed> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(map((followerXFollowed: HttpResponse<FollowerXFollowed>) => followerXFollowed.body));
    }
    return of(new FollowerXFollowed());
  }
}

export const followerXFollowedRoute: Routes = [
  {
    path: '',
    component: FollowerXFollowedComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'FollowerXFolloweds'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: FollowerXFollowedDetailComponent,
    resolve: {
      followerXFollowed: FollowerXFollowedResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'FollowerXFolloweds'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: FollowerXFollowedUpdateComponent,
    resolve: {
      followerXFollowed: FollowerXFollowedResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'FollowerXFolloweds'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: FollowerXFollowedUpdateComponent,
    resolve: {
      followerXFollowed: FollowerXFollowedResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'FollowerXFolloweds'
    },
    canActivate: [UserRouteAccessService]
  }
];
