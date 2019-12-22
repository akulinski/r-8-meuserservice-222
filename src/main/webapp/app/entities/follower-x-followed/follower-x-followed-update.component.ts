import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';
import { IFollowerXFollowed, FollowerXFollowed } from 'app/shared/model/follower-x-followed.model';
import { FollowerXFollowedService } from './follower-x-followed.service';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { UserProfileService } from 'app/entities/user-profile/user-profile.service';

@Component({
  selector: 'jhi-follower-x-followed-update',
  templateUrl: './follower-x-followed-update.component.html'
})
export class FollowerXFollowedUpdateComponent implements OnInit {
  isSaving: boolean;

  followers: IUserProfile[];

  followeds: IUserProfile[];

  editForm = this.fb.group({
    id: [],
    followerId: [],
    followedId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected followerXFollowedService: FollowerXFollowedService,
    protected userProfileService: UserProfileService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ followerXFollowed }) => {
      this.updateForm(followerXFollowed);
    });
    this.userProfileService.query({ filter: 'followerxfollowed-is-null' }).subscribe(
      (res: HttpResponse<IUserProfile[]>) => {
        if (!this.editForm.get('followerId').value) {
          this.followers = res.body;
        } else {
          this.userProfileService
            .find(this.editForm.get('followerId').value)
            .subscribe(
              (subRes: HttpResponse<IUserProfile>) => (this.followers = [subRes.body].concat(res.body)),
              (subRes: HttpErrorResponse) => this.onError(subRes.message)
            );
        }
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
    this.userProfileService.query({ filter: 'followerxfollowed-is-null' }).subscribe(
      (res: HttpResponse<IUserProfile[]>) => {
        if (!this.editForm.get('followedId').value) {
          this.followeds = res.body;
        } else {
          this.userProfileService
            .find(this.editForm.get('followedId').value)
            .subscribe(
              (subRes: HttpResponse<IUserProfile>) => (this.followeds = [subRes.body].concat(res.body)),
              (subRes: HttpErrorResponse) => this.onError(subRes.message)
            );
        }
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
  }

  updateForm(followerXFollowed: IFollowerXFollowed) {
    this.editForm.patchValue({
      id: followerXFollowed.id,
      followerId: followerXFollowed.followerId,
      followedId: followerXFollowed.followedId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const followerXFollowed = this.createFromForm();
    if (followerXFollowed.id !== undefined) {
      this.subscribeToSaveResponse(this.followerXFollowedService.update(followerXFollowed));
    } else {
      this.subscribeToSaveResponse(this.followerXFollowedService.create(followerXFollowed));
    }
  }

  private createFromForm(): IFollowerXFollowed {
    return {
      ...new FollowerXFollowed(),
      id: this.editForm.get(['id']).value,
      followerId: this.editForm.get(['followerId']).value,
      followedId: this.editForm.get(['followedId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFollowerXFollowed>>) {
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
}
