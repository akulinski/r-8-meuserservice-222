import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IFollowerXFollowed } from 'app/shared/model/follower-x-followed.model';
import { FollowerXFollowedService } from './follower-x-followed.service';
import { FollowerXFollowedDeleteDialogComponent } from './follower-x-followed-delete-dialog.component';

@Component({
  selector: 'jhi-follower-x-followed',
  templateUrl: './follower-x-followed.component.html'
})
export class FollowerXFollowedComponent implements OnInit, OnDestroy {
  followerXFolloweds: IFollowerXFollowed[];
  eventSubscriber: Subscription;
  currentSearch: string;

  constructor(
    protected followerXFollowedService: FollowerXFollowedService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected activatedRoute: ActivatedRoute
  ) {
    this.currentSearch =
      this.activatedRoute.snapshot && this.activatedRoute.snapshot.queryParams['search']
        ? this.activatedRoute.snapshot.queryParams['search']
        : '';
  }

  loadAll() {
    if (this.currentSearch) {
      this.followerXFollowedService
        .search({
          query: this.currentSearch
        })
        .subscribe((res: HttpResponse<IFollowerXFollowed[]>) => (this.followerXFolloweds = res.body));
      return;
    }
    this.followerXFollowedService.query().subscribe((res: HttpResponse<IFollowerXFollowed[]>) => {
      this.followerXFolloweds = res.body;
      this.currentSearch = '';
    });
  }

  search(query) {
    if (!query) {
      return this.clear();
    }
    this.currentSearch = query;
    this.loadAll();
  }

  clear() {
    this.currentSearch = '';
    this.loadAll();
  }

  ngOnInit() {
    this.loadAll();
    this.registerChangeInFollowerXFolloweds();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IFollowerXFollowed) {
    return item.id;
  }

  registerChangeInFollowerXFolloweds() {
    this.eventSubscriber = this.eventManager.subscribe('followerXFollowedListModification', () => this.loadAll());
  }

  delete(followerXFollowed: IFollowerXFollowed) {
    const modalRef = this.modalService.open(FollowerXFollowedDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.followerXFollowed = followerXFollowed;
  }
}
