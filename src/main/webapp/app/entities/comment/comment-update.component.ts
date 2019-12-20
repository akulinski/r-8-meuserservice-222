import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IComment, Comment } from 'app/shared/model/comment.model';
import { CommentService } from './comment.service';
import { ICommentXProfile } from 'app/shared/model/comment-x-profile.model';
import { CommentXProfileService } from 'app/entities/comment-x-profile/comment-x-profile.service';

@Component({
  selector: 'jhi-comment-update',
  templateUrl: './comment-update.component.html'
})
export class CommentUpdateComponent implements OnInit {
  isSaving: boolean;

  commentxprofiles: ICommentXProfile[];

  editForm = this.fb.group({
    id: [],
    comment: [],
    timeStamp: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected commentService: CommentService,
    protected commentXProfileService: CommentXProfileService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ comment }) => {
      this.updateForm(comment);
    });
    this.commentXProfileService
      .query()
      .subscribe(
        (res: HttpResponse<ICommentXProfile[]>) => (this.commentxprofiles = res.body),
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  updateForm(comment: IComment) {
    this.editForm.patchValue({
      id: comment.id,
      comment: comment.comment,
      timeStamp: comment.timeStamp != null ? comment.timeStamp.format(DATE_TIME_FORMAT) : null
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const comment = this.createFromForm();
    if (comment.id !== undefined) {
      this.subscribeToSaveResponse(this.commentService.update(comment));
    } else {
      this.subscribeToSaveResponse(this.commentService.create(comment));
    }
  }

  private createFromForm(): IComment {
    return {
      ...new Comment(),
      id: this.editForm.get(['id']).value,
      comment: this.editForm.get(['comment']).value,
      timeStamp:
        this.editForm.get(['timeStamp']).value != null ? moment(this.editForm.get(['timeStamp']).value, DATE_TIME_FORMAT) : undefined
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IComment>>) {
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

  trackCommentXProfileById(index: number, item: ICommentXProfile) {
    return item.id;
  }
}
