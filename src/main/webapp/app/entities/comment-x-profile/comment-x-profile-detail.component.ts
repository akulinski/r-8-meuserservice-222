import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICommentXProfile } from 'app/shared/model/comment-x-profile.model';

@Component({
  selector: 'jhi-comment-x-profile-detail',
  templateUrl: './comment-x-profile-detail.component.html'
})
export class CommentXProfileDetailComponent implements OnInit {
  commentXProfile: ICommentXProfile;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ commentXProfile }) => {
      this.commentXProfile = commentXProfile;
    });
  }

  previousState() {
    window.history.back();
  }
}
