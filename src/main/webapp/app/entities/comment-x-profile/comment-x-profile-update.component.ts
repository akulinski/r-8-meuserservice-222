import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';
import { ICommentXProfile, CommentXProfile } from 'app/shared/model/comment-x-profile.model';
import { CommentXProfileService } from './comment-x-profile.service';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { UserProfileService } from 'app/entities/user-profile/user-profile.service';
import { IComment } from 'app/shared/model/comment.model';
import { CommentService } from 'app/entities/comment/comment.service';

@Component({
  selector: 'jhi-comment-x-profile-update',
  templateUrl: './comment-x-profile-update.component.html'
})
export class CommentXProfileUpdateComponent implements OnInit {
  isSaving: boolean;

  receivers: IUserProfile[];

  posters: IUserProfile[];

  comments: IComment[];

  editForm = this.fb.group({
    id: [],
    receiver: [],
    poster: [],
    comment: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected commentXProfileService: CommentXProfileService,
    protected userProfileService: UserProfileService,
    protected commentService: CommentService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ commentXProfile }) => {
      this.updateForm(commentXProfile);
    });
    this.userProfileService.query({ filter: 'commentxprofile-is-null' }).subscribe(
      (res: HttpResponse<IUserProfile[]>) => {
        if (!this.editForm.get('receiver').value || !this.editForm.get('receiver').value.id) {
          this.receivers = res.body;
        } else {
          this.userProfileService
            .find(this.editForm.get('receiver').value.id)
            .subscribe(
              (subRes: HttpResponse<IUserProfile>) => (this.receivers = [subRes.body].concat(res.body)),
              (subRes: HttpErrorResponse) => this.onError(subRes.message)
            );
        }
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
    this.userProfileService.query({ filter: 'commentxprofile-is-null' }).subscribe(
      (res: HttpResponse<IUserProfile[]>) => {
        if (!this.editForm.get('poster').value || !this.editForm.get('poster').value.id) {
          this.posters = res.body;
        } else {
          this.userProfileService
            .find(this.editForm.get('poster').value.id)
            .subscribe(
              (subRes: HttpResponse<IUserProfile>) => (this.posters = [subRes.body].concat(res.body)),
              (subRes: HttpErrorResponse) => this.onError(subRes.message)
            );
        }
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
    this.commentService.query({ filter: 'commentxprofile-is-null' }).subscribe(
      (res: HttpResponse<IComment[]>) => {
        if (!this.editForm.get('comment').value || !this.editForm.get('comment').value.id) {
          this.comments = res.body;
        } else {
          this.commentService
            .find(this.editForm.get('comment').value.id)
            .subscribe(
              (subRes: HttpResponse<IComment>) => (this.comments = [subRes.body].concat(res.body)),
              (subRes: HttpErrorResponse) => this.onError(subRes.message)
            );
        }
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
  }

  updateForm(commentXProfile: ICommentXProfile) {
    this.editForm.patchValue({
      id: commentXProfile.id,
      receiver: commentXProfile.receiver,
      poster: commentXProfile.poster,
      comment: commentXProfile.comment
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const commentXProfile = this.createFromForm();
    if (commentXProfile.id !== undefined) {
      this.subscribeToSaveResponse(this.commentXProfileService.update(commentXProfile));
    } else {
      this.subscribeToSaveResponse(this.commentXProfileService.create(commentXProfile));
    }
  }

  private createFromForm(): ICommentXProfile {
    return {
      ...new CommentXProfile(),
      id: this.editForm.get(['id']).value,
      receiver: this.editForm.get(['receiver']).value,
      poster: this.editForm.get(['poster']).value,
      comment: this.editForm.get(['comment']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICommentXProfile>>) {
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

  trackCommentById(index: number, item: IComment) {
    return item.id;
  }
}
