import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';
import { IRateXProfile, RateXProfile } from 'app/shared/model/rate-x-profile.model';
import { RateXProfileService } from './rate-x-profile.service';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { UserProfileService } from 'app/entities/user-profile/user-profile.service';
import { IRate } from 'app/shared/model/rate.model';
import { RateService } from 'app/entities/rate/rate.service';

@Component({
  selector: 'jhi-rate-x-profile-update',
  templateUrl: './rate-x-profile-update.component.html'
})
export class RateXProfileUpdateComponent implements OnInit {
  isSaving: boolean;

  rateds: IUserProfile[];

  raters: IUserProfile[];

  rates: IRate[];

  editForm = this.fb.group({
    id: [],
    ratedId: [],
    raterId: [],
    rateId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected rateXProfileService: RateXProfileService,
    protected userProfileService: UserProfileService,
    protected rateService: RateService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ rateXProfile }) => {
      this.updateForm(rateXProfile);
    });
    this.userProfileService.query({ filter: 'ratexprofile-is-null' }).subscribe(
      (res: HttpResponse<IUserProfile[]>) => {
        if (!this.editForm.get('ratedId').value) {
          this.rateds = res.body;
        } else {
          this.userProfileService
            .find(this.editForm.get('ratedId').value)
            .subscribe(
              (subRes: HttpResponse<IUserProfile>) => (this.rateds = [subRes.body].concat(res.body)),
              (subRes: HttpErrorResponse) => this.onError(subRes.message)
            );
        }
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
    this.userProfileService.query({ filter: 'ratexprofile-is-null' }).subscribe(
      (res: HttpResponse<IUserProfile[]>) => {
        if (!this.editForm.get('raterId').value) {
          this.raters = res.body;
        } else {
          this.userProfileService
            .find(this.editForm.get('raterId').value)
            .subscribe(
              (subRes: HttpResponse<IUserProfile>) => (this.raters = [subRes.body].concat(res.body)),
              (subRes: HttpErrorResponse) => this.onError(subRes.message)
            );
        }
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
    this.rateService.query({ filter: 'ratexprofile-is-null' }).subscribe(
      (res: HttpResponse<IRate[]>) => {
        if (!this.editForm.get('rateId').value) {
          this.rates = res.body;
        } else {
          this.rateService
            .find(this.editForm.get('rateId').value)
            .subscribe(
              (subRes: HttpResponse<IRate>) => (this.rates = [subRes.body].concat(res.body)),
              (subRes: HttpErrorResponse) => this.onError(subRes.message)
            );
        }
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
  }

  updateForm(rateXProfile: IRateXProfile) {
    this.editForm.patchValue({
      id: rateXProfile.id,
      ratedId: rateXProfile.ratedId,
      raterId: rateXProfile.raterId,
      rateId: rateXProfile.rateId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const rateXProfile = this.createFromForm();
    if (rateXProfile.id !== undefined) {
      this.subscribeToSaveResponse(this.rateXProfileService.update(rateXProfile));
    } else {
      this.subscribeToSaveResponse(this.rateXProfileService.create(rateXProfile));
    }
  }

  private createFromForm(): IRateXProfile {
    return {
      ...new RateXProfile(),
      id: this.editForm.get(['id']).value,
      ratedId: this.editForm.get(['ratedId']).value,
      raterId: this.editForm.get(['raterId']).value,
      rateId: this.editForm.get(['rateId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRateXProfile>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackUserProfileById(index: number, item: IUserProfile) {
    return item.id;
  }

  trackRateById(index: number, item: IRate) {
    return item.id;
  }
}
