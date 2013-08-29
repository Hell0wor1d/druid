package com.metamx.druid.guice;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Binder;
import com.google.inject.multibindings.MapBinder;
import com.metamx.druid.query.group.GroupByQuery;
import com.metamx.druid.query.group.GroupByQueryEngine;
import com.metamx.druid.query.group.GroupByQueryRunnerFactory;
import com.metamx.druid.query.metadata.SegmentMetadataQuery;
import com.metamx.druid.query.metadata.SegmentMetadataQueryRunnerFactory;
import com.metamx.druid.query.search.SearchQuery;
import com.metamx.druid.query.search.SearchQueryRunnerFactory;
import com.metamx.druid.query.timeboundary.TimeBoundaryQuery;
import com.metamx.druid.query.timeboundary.TimeBoundaryQueryRunnerFactory;
import com.metamx.druid.query.timeseries.TimeseriesQuery;
import com.metamx.druid.query.timeseries.TimeseriesQueryRunnerFactory;
import io.druid.initialization.Binders;
import io.druid.query.Query;
import io.druid.query.QueryRunnerFactory;

import java.util.Map;

/**
 */
public class QueryRunnerFactoryModule extends QueryToolChestModule
{
  private static final Map<Class<? extends Query>, Class<? extends QueryRunnerFactory>> mappings =
      ImmutableMap.<Class<? extends Query>, Class<? extends QueryRunnerFactory>>builder()
                  .put(TimeseriesQuery.class, TimeseriesQueryRunnerFactory.class)
                  .put(SearchQuery.class, SearchQueryRunnerFactory.class)
                  .put(TimeBoundaryQuery.class, TimeBoundaryQueryRunnerFactory.class)
                  .put(SegmentMetadataQuery.class, SegmentMetadataQueryRunnerFactory.class)
                  .put(GroupByQuery.class, GroupByQueryRunnerFactory.class)
                  .build();

  @Override
  public void configure(Binder binder)
  {
    super.configure(binder);


    final MapBinder<Class<? extends Query>, QueryRunnerFactory> queryFactoryBinder = Binders.queryFactoryBinder(binder);

    for (Map.Entry<Class<? extends Query>, Class<? extends QueryRunnerFactory>> entry : mappings.entrySet()) {
      queryFactoryBinder.addBinding(entry.getKey()).to(entry.getValue());
      binder.bind(entry.getValue()).in(LazySingleton.class);
    }

    binder.bind(GroupByQueryEngine.class).in(LazySingleton.class);
  }
}