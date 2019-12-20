import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { CommentXProfile } from 'app/shared/model/comment-x-profile.model';
import { CommentXProfileService } from './comment-x-profile.service';
import { CommentXProfileComponent } from './comment-x-profile.component';
import { CommentXProfileDetailComponent } from './comment-x-profile-detail.component';
import { CommentXProfileUpdateComponent } from './comment-x-profile-update.component';
import { ICommentXProfile } from 'app/shared/model/comment-x-profile.model';

@Injectable({ providedIn: 'root' })
export class CommentXProfileResolve implements Resolve<ICommentXProfile> {
  constructor(private service: CommentXProfileService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICommentXProfile> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(map((commentXProfile: HttpResponse<CommentXProfile>) => commentXProfile.body));
    }
    return of(new CommentXProfile());
  }
}

export const commentXProfileRoute: Routes = [
  {
    path: '',
    component: CommentXProfileComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CommentXProfiles'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: CommentXProfileDetailComponent,
    resolve: {
      commentXProfile: CommentXProfileResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CommentXProfiles'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: CommentXProfileUpdateComponent,
    resolve: {
      commentXProfile: CommentXProfileResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CommentXProfiles'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: CommentXProfileUpdateComponent,
    resolve: {
      commentXProfile: CommentXProfileResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'CommentXProfiles'
    },
    canActivate: [UserRouteAccessService]
  }
];
