import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { R8Meuserservice2SharedModule } from 'app/shared/shared.module';
import { CommentXProfileComponent } from './comment-x-profile.component';
import { CommentXProfileDetailComponent } from './comment-x-profile-detail.component';
import { CommentXProfileUpdateComponent } from './comment-x-profile-update.component';
import { CommentXProfileDeleteDialogComponent } from './comment-x-profile-delete-dialog.component';
import { commentXProfileRoute } from './comment-x-profile.route';

@NgModule({
  imports: [R8Meuserservice2SharedModule, RouterModule.forChild(commentXProfileRoute)],
  declarations: [
    CommentXProfileComponent,
    CommentXProfileDetailComponent,
    CommentXProfileUpdateComponent,
    CommentXProfileDeleteDialogComponent
  ],
  entryComponents: [CommentXProfileDeleteDialogComponent]
})
export class R8Meuserservice2CommentXProfileModule {}
