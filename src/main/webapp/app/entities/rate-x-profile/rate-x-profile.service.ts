import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IRateXProfile } from 'app/shared/model/rate-x-profile.model';

type EntityResponseType = HttpResponse<IRateXProfile>;
type EntityArrayResponseType = HttpResponse<IRateXProfile[]>;

@Injectable({ providedIn: 'root' })
export class RateXProfileService {
  public resourceUrl = SERVER_API_URL + 'api/rate-x-profiles';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/rate-x-profiles';

  constructor(protected http: HttpClient) {}

  create(rateXProfile: IRateXProfile): Observable<EntityResponseType> {
    return this.http.post<IRateXProfile>(this.resourceUrl, rateXProfile, { observe: 'response' });
  }

  update(rateXProfile: IRateXProfile): Observable<EntityResponseType> {
    return this.http.put<IRateXProfile>(this.resourceUrl, rateXProfile, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRateXProfile>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRateXProfile[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRateXProfile[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
