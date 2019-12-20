import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ICommentXProfile } from 'app/shared/model/comment-x-profile.model';

type EntityResponseType = HttpResponse<ICommentXProfile>;
type EntityArrayResponseType = HttpResponse<ICommentXProfile[]>;

@Injectable({ providedIn: 'root' })
export class CommentXProfileService {
  public resourceUrl = SERVER_API_URL + 'api/comment-x-profiles';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/comment-x-profiles';

  constructor(protected http: HttpClient) {}

  create(commentXProfile: ICommentXProfile): Observable<EntityResponseType> {
    return this.http.post<ICommentXProfile>(this.resourceUrl, commentXProfile, { observe: 'response' });
  }

  update(commentXProfile: ICommentXProfile): Observable<EntityResponseType> {
    return this.http.put<ICommentXProfile>(this.resourceUrl, commentXProfile, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICommentXProfile>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICommentXProfile[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICommentXProfile[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
