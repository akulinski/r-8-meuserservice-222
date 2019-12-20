import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFollowerXFollowed } from 'app/shared/model/follower-x-followed.model';

@Component({
  selector: 'jhi-follower-x-followed-detail',
  templateUrl: './follower-x-followed-detail.component.html'
})
export class FollowerXFollowedDetailComponent implements OnInit {
  followerXFollowed: IFollowerXFollowed;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ followerXFollowed }) => {
      this.followerXFollowed = followerXFollowed;
    });
  }

  previousState() {
    window.history.back();
  }
}
