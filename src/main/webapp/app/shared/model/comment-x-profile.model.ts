export interface ICommentXProfile {
  id?: number;
  receiverId?: number;
  posterId?: number;
  commentId?: number;
}

export class CommentXProfile implements ICommentXProfile {
  constructor(public id?: number, public receiverId?: number, public posterId?: number, public commentId?: number) {}
}
