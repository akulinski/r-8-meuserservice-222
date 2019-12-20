import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { RateXProfile } from 'app/shared/model/rate-x-profile.model';
import { RateXProfileService } from './rate-x-profile.service';
import { RateXProfileComponent } from './rate-x-profile.component';
import { RateXProfileDetailComponent } from './rate-x-profile-detail.component';
import { RateXProfileUpdateComponent } from './rate-x-profile-update.component';
import { IRateXProfile } from 'app/shared/model/rate-x-profile.model';

@Injectable({ providedIn: 'root' })
export class RateXProfileResolve implements Resolve<IRateXProfile> {
  constructor(private service: RateXProfileService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRateXProfile> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(map((rateXProfile: HttpResponse<RateXProfile>) => rateXProfile.body));
    }
    return of(new RateXProfile());
  }
}

export const rateXProfileRoute: Routes = [
  {
    path: '',
    component: RateXProfileComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'RateXProfiles'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: RateXProfileDetailComponent,
    resolve: {
      rateXProfile: RateXProfileResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'RateXProfiles'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: RateXProfileUpdateComponent,
    resolve: {
      rateXProfile: RateXProfileResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'RateXProfiles'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: RateXProfileUpdateComponent,
    resolve: {
      rateXProfile: RateXProfileResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'RateXProfiles'
    },
    canActivate: [UserRouteAccessService]
  }
];
