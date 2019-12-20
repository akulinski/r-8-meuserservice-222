import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICommentXProfile } from 'app/shared/model/comment-x-profile.model';
import { CommentXProfileService } from './comment-x-profile.service';
import { CommentXProfileDeleteDialogComponent } from './comment-x-profile-delete-dialog.component';

@Component({
  selector: 'jhi-comment-x-profile',
  templateUrl: './comment-x-profile.component.html'
})
export class CommentXProfileComponent implements OnInit, OnDestroy {
  commentXProfiles: ICommentXProfile[];
  eventSubscriber: Subscription;
  currentSearch: string;

  constructor(
    protected commentXProfileService: CommentXProfileService,
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
      this.commentXProfileService
        .search({
          query: this.currentSearch
        })
        .subscribe((res: HttpResponse<ICommentXProfile[]>) => (this.commentXProfiles = res.body));
      return;
    }
    this.commentXProfileService.query().subscribe((res: HttpResponse<ICommentXProfile[]>) => {
      this.commentXProfiles = res.body;
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
    this.registerChangeInCommentXProfiles();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: ICommentXProfile) {
    return item.id;
  }

  registerChangeInCommentXProfiles() {
    this.eventSubscriber = this.eventManager.subscribe('commentXProfileListModification', () => this.loadAll());
  }

  delete(commentXProfile: ICommentXProfile) {
    const modalRef = this.modalService.open(CommentXProfileDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.commentXProfile = commentXProfile;
  }
}
