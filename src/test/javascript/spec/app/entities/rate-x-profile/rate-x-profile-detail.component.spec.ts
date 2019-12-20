import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { R8Meuserservice2TestModule } from '../../../test.module';
import { RateXProfileDetailComponent } from 'app/entities/rate-x-profile/rate-x-profile-detail.component';
import { RateXProfile } from 'app/shared/model/rate-x-profile.model';

describe('Component Tests', () => {
  describe('RateXProfile Management Detail Component', () => {
    let comp: RateXProfileDetailComponent;
    let fixture: ComponentFixture<RateXProfileDetailComponent>;
    const route = ({ data: of({ rateXProfile: new RateXProfile(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [R8Meuserservice2TestModule],
        declarations: [RateXProfileDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(RateXProfileDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RateXProfileDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.rateXProfile).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
