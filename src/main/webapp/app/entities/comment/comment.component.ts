import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IComment } from 'app/shared/model/comment.model';
import { CommentService } from './comment.service';
import { CommentDeleteDialogComponent } from './comment-delete-dialog.component';

@Component({
  selector: 'jhi-comment',
  templateUrl: './comment.component.html'
})
export class CommentComponent implements OnInit, OnDestroy {
  comments: IComment[];
  eventSubscriber: Subscription;
  currentSearch: string;

  constructor(
    protected commentService: CommentService,
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
      this.commentService
        .search({
          query: this.currentSearch
        })
        .subscribe((res: HttpResponse<IComment[]>) => (this.comments = res.body));
      return;
    }
    this.commentService.query().subscribe((res: HttpResponse<IComment[]>) => {
      this.comments = res.body;
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
    this.registerChangeInComments();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IComment) {
    return item.id;
  }

  registerChangeInComments() {
    this.eventSubscriber = this.eventManager.subscribe('commentListModification', () => this.loadAll());
  }

  delete(comment: IComment) {
    const modalRef = this.modalService.open(CommentDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.comment = comment;
  }
}
